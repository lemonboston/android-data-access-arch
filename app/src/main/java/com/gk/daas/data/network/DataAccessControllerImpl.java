package com.gk.daas.data.network;

import com.gk.daas.bus.Bus;
import com.gk.daas.data.access.DataAccessController;
import com.gk.daas.data.event.GetForecastProgressEvent;
import com.gk.daas.data.event.GetForecastSuccessEvent;
import com.gk.daas.data.event.GetTempStoreSuccessEvent;
import com.gk.daas.data.event.GetTempSuccessEvent;
import com.gk.daas.data.model.ForecastResponse;
import com.gk.daas.data.model.WeatherResponse;
import com.gk.daas.data.network.connection.NetworkConnectionChecker;
import com.gk.daas.data.store.DataStore;
import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

import rx.Single;
import rx.Subscription;
import rx.schedulers.Schedulers;

import static com.gk.daas.data.network.OpenWeatherService.API_KEY;

/**
 * @author Gabor_Keszthelyi
 */
// TODO add data mapping to chain (to Temperature from json object) - Temperature representation is not trivial
public class DataAccessControllerImpl implements DataAccessController {

    private final OpenWeatherService weatherService;
    private final Log log;
    private final Bus bus;
    private final NetworkConnectionChecker connectionChecker;
    private final DataStore dataStore;
    private final TaskCounter taskCounter;
    private final ErrorInterpreter errorInterpreter;

    private Subscription getTempWithOngoingHandlingSubscription;
    private Subscription getTempAllInOneSubscription;

    public DataAccessControllerImpl(OpenWeatherService weatherService, LogFactory logFactory, Bus bus, NetworkConnectionChecker connectionChecker, DataStore dataStore, TaskCounter taskCounter, ErrorInterpreter errorInterpreter) {
        this.weatherService = weatherService;
        this.bus = bus;
        this.connectionChecker = connectionChecker;
        this.dataStore = dataStore;
        this.taskCounter = taskCounter;
        this.errorInterpreter = errorInterpreter;
        this.log = logFactory.create(getClass());
    }

    @Override
    public void getWeather(UseCase useCase, String city) {
        switch (useCase) {
            case BASIC:
                getTemperature_basic(city);
                break;
            case ERROR_HANDLING:
                getTemperature_wErrorHandling(city);
                break;
            case ONGOING_CALL_HANDLING:
                getTemperature_wOngoingHandling(city);
                break;
            case OFFLINE_STORAGE:
                getTemperature_wOfflineLocalStore(city);
                break;
            case COMBINED:
                getTemperature_allInOne(city);
                break;
        }
    }

    private void getTemperature_basic(String city) {
        String tag = "getTemperature_basic | ";
        log.d(tag + "Starting, city: " + city);

        weatherService.getWeather(city, API_KEY)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        (WeatherResponse weatherResponse) -> {
                            double temp = weatherResponse.main.temp;
                            log.d(tag + "Finished, temp returned: " + temp);
                            bus.post(new GetTempSuccessEvent(temp));
                            taskCounter.taskFinished();
                        });
    }

    private void getTemperature_wErrorHandling(String city) {
        String tag = "getTemperature_wErrorHandling | ";
        log.d(tag + "Starting, city: " + city);

        connectionChecker.checkNetwork()
                .flatMap(aVoid -> weatherService.getWeather(city, "an invalid api key"))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        (WeatherResponse weatherResponse) -> {
                            double temp = weatherResponse.main.temp;
                            log.d(tag + "Finished, temp returned: " + temp);
                            bus.post(new GetTempSuccessEvent(temp));
                            taskCounter.taskFinished();
                        },
                        throwable -> {
                            DataAccessError dataAccessError = errorInterpreter.interpret(throwable);
                            bus.post(dataAccessError);
                            log.w(tag + "Error: " + throwable);
                            taskCounter.taskFinished();
                        });
    }

    private void getTemperature_wOngoingHandling(String city) {
        String tag = "getTemperature_wOngoingHandling |";
        log.d(tag + "Starting, city: " + city);

        if (isOngoing(getTempWithOngoingHandlingSubscription)) {
            log.d(tag + "Is already ongoing, ignoring request.");
            taskCounter.taskFinished();
            return;
        }

        getTempWithOngoingHandlingSubscription = weatherService.getWeather(city, API_KEY)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        (WeatherResponse weatherResponse) -> {
                            double temp = weatherResponse.main.temp;
                            log.d(tag + "Finished, temp returned: " + temp);
                            bus.post(new GetTempSuccessEvent(temp));
                            taskCounter.taskFinished();
                        });
    }

    private void getTemperature_wOfflineLocalStore(String city) {
        String tag = "getTemperature_wOfflineLocalStore | ";
        log.d(tag + "Starting, city: " + city);

        weatherService.getWeather(city, API_KEY)

                .doOnSuccess((WeatherResponse weatherResponse) -> {
                    log.d(tag + "Saving temp to data store");
                    dataStore.saveAsync(DataStore.GET_TEMP, weatherResponse);
                })

                .toObservable()
                .onErrorResumeNext(throwable -> {
                    log.w(tag + "Error getting temp through API: " + throwable);
                    return dataStore.getAsSingle(DataStore.GET_TEMP).toObservable();
                })
                .toSingle()

                .subscribeOn(Schedulers.io())

                .subscribe(
                        (WeatherResponse weatherResponse) -> {
                            double temp = weatherResponse.main.temp;
                            log.d(tag + "Temp retrieved (from API or local store), temp: " + temp);
                            bus.post(new GetTempStoreSuccessEvent(temp));
                            taskCounter.taskFinished();
                        },
                        throwable -> {
                            log.w(tag + "Error: " + throwable);
                            taskCounter.taskFinished();
                        });

    }

    // TODO Case with retry
    // http://reactivex.io/documentation/operators/retry.html
    // check the delay example

    // TODO Case with cancel

    public void getTemperature_allInOne(String city) {
        String tag = "getTemperature_allInOne | ";
        log.d(tag + "Starting, city: " + city);

        if (isOngoing(getTempAllInOneSubscription)) {
            log.d(tag + " is already ongoing, ignoring request.");
            taskCounter.taskFinished();
            return;
        }

        getTempAllInOneSubscription =
                connectionChecker.checkNetwork()

                        .flatMap(aVoid -> weatherService.getWeather(city, API_KEY))

                        .doOnSuccess((WeatherResponse weatherResponse) -> {
                            log.d(tag + "Saving temp to data store");
                            dataStore.saveAsync(DataStore.GET_TEMP, weatherResponse);
                        })

                        .toObservable()
                        .onErrorResumeNext(throwable -> {
                            log.w(tag + "Error getting temp through API: " + throwable);
                            return dataStore.getAsSingle(DataStore.GET_TEMP).toObservable();
                        })
                        .toSingle()

                        .subscribeOn(Schedulers.io())

                        .subscribe(
                                (WeatherResponse weatherResponse) -> {
                                    double temp = weatherResponse.main.temp;
                                    log.d(tag + "Temp retrieved (from API or local store), temp: " + temp);
                                    bus.post(new GetTempStoreSuccessEvent(temp));
                                    taskCounter.taskFinished();
                                },
                                throwable -> {
                                    DataAccessError dataAccessError = errorInterpreter.interpret(throwable);
                                    bus.post(dataAccessError);
                                    log.w(tag + "Error: " + throwable);
                                    taskCounter.taskFinished();
                                });
    }

    @Override
    // TODO review
    public void getForecastForWarmerCity(String city1, String city2) {
        String tag = "getForecastForWarmerCity | ";
        log.d(tag + "Started");
        bus.postSticky(GetForecastProgressEvent.FIRST_STAGE_STARTED);

        Single.zip(
                weatherService.getWeather(city1, API_KEY).subscribeOn(Schedulers.newThread()),
                weatherService.getWeather(city2, API_KEY).subscribeOn(Schedulers.newThread()),
                (response1, response2) -> response1.main.temp > response2.main.temp ? city1 : city2)

                .doOnSuccess(city -> bus.postSticky(GetForecastProgressEvent.SECOND_STAGE_STARTED))

                .flatMap(city -> weatherService.getForecast(city, API_KEY))

                .subscribeOn(Schedulers.io())

                .subscribe(
                        (ForecastResponse response) -> {
                            // TODO converter/mapper
                            double lastTemp = response.list.get(response.list.size() - 1).main.temp;
                            String cityName = response.city.name;
                            log.d(tag + "Finished, city: " + cityName + " , lastTemp: " + lastTemp);
                            bus.post(new GetForecastSuccessEvent(lastTemp, cityName));
                            bus.postSticky(GetForecastProgressEvent.COMPLETED);
                            taskCounter.taskFinished();
                        },
                        throwable -> {
                            log.w(tag + "Error: " + throwable);
                            taskCounter.taskFinished();
                        }
                );

    }

    private boolean isOngoing(Subscription subscription) {
        return subscription != null && !subscription.isUnsubscribed();
    }
}
