package com.gk.daas.data.network;

import com.gk.daas.bus.Bus;
import com.gk.daas.data.access.DataAccessController;
import com.gk.daas.data.event.GetDiffSuccessEvent;
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
import rx.SingleSubscriber;
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

    private Subscription forecastSubscription;
    private Subscription getTempDiffSubscription;

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
    public void getTemperature(String city) {
        log.d("GetTemp call starting, city: " + city);

        connectionChecker.checkNetwork()

                .flatMap(aVoid -> weatherService.getWeather(city, API_KEY))

                .subscribe(
                        (WeatherResponse weatherResponse) -> {
                            double temp = weatherResponse.main.temp;
                            log.d("Temp returned: " + temp);
                            bus.post(new GetTempSuccessEvent(temp));
                            taskCounter.taskFinished();
                        },
                        throwable -> {
                            DataAccessError dataAccessError = errorInterpreter.interpret(throwable);
                            bus.post(dataAccessError);
                            log.e("Get temp failed", throwable);
                            taskCounter.taskFinished();
                        });
    }

    @Override
    public void getForecastForWarmestCity(String city1, String city2) {
        log.d("getForecastForWarmestCity call started");

        if (isOngoing(forecastSubscription)) {
            log.d("Getting forecast is already ongoing, ignoring request.");
            taskCounter.taskFinished();
            return;
        }

        bus.postSticky(GetForecastProgressEvent.FIRST_STAGE_STARTED);

        forecastSubscription = Single.zip(
                weatherService.getWeather(city1, API_KEY).subscribeOn(Schedulers.newThread()),
                weatherService.getWeather(city2, API_KEY).subscribeOn(Schedulers.newThread()),
                (response1, response2) -> response1.main.temp > response2.main.temp ? city1 : city2)

                .doOnSuccess(city -> bus.postSticky(GetForecastProgressEvent.SECOND_STAGE_STARTED))

                .flatMap(city -> weatherService.getForecast(city, API_KEY))

                .subscribeOn(Schedulers.newThread())

                .subscribe(
                        (ForecastResponse response) -> {
                            log.d("getForecastForWarmestCity call finished");
                            double lastTemp = response.list.get(response.list.size() - 1).main.temp;
                            bus.post(new GetForecastSuccessEvent(lastTemp));
                            bus.postSticky(GetForecastProgressEvent.COMPLETED);
                            taskCounter.taskFinished();
                        },
                        throwable -> {
                            log.e("GetForecastForWarmestCity error", throwable);
                            taskCounter.taskFinished();
                        }
                );

    }

    @Override
    public void getTemperatureDiff(String city1, String city2) {
        log.d("Get diff chain starting");

        if (isOngoing(getTempDiffSubscription)) {
            log.d("Getting temp diff is already ongoing, ignoring request.");
            taskCounter.taskFinished();
            return;
        }

        // How to make them parallel http://stackoverflow.com/a/21208440/4247460
        getTempDiffSubscription = Single.zip(
                weatherService.getWeather(city1, API_KEY).subscribeOn(Schedulers.newThread()),
                weatherService.getWeather(city2, API_KEY).subscribeOn(Schedulers.newThread()),
                (response1, response2) -> response1.main.temp - response2.main.temp)

                .subscribe(new SingleSubscriber<Double>() {
                    @Override
                    public void onSuccess(Double value) {
                        log.d("Get diff chain finished");
                        bus.post(new GetDiffSuccessEvent(value));
                        taskCounter.taskFinished();
                    }

                    @Override
                    public void onError(Throwable error) {
                        log.e("Get diff chain error", error);
                        taskCounter.taskFinished();
                    }
                });
    }

    @Override
    public void getTemperature_OfflineLocalStore(String city) {
        if (connectionChecker.isNetworkAvailable()) {
            weatherService.getWeather(city, API_KEY)
                    .doOnSuccess(weatherResponse -> {
                        double temp = weatherResponse.main.temp;
                        log.d("Temp returned: " + temp);
                        bus.post(new GetTempStoreSuccessEvent(temp));
                        taskCounter.taskFinished();
                    })
                    .doOnError(throwable1 -> {
                        log.e("Get temp (offline support) failed");
                        taskCounter.taskFinished();
                    })
                    .doOnSuccess(weatherResponse -> {
                        log.d("Saving to store");
                        dataStore.save(DataStore.GET_TEMP, weatherResponse);
                    })
                    .subscribe();
        } else {
            WeatherResponse weatherResponse = dataStore.get(DataStore.GET_TEMP);
            if (weatherResponse != null) {
                bus.post(new GetTempStoreSuccessEvent(weatherResponse.main.temp));
            }
            taskCounter.taskFinished();
        }
    }

    private boolean isOngoing(Subscription subscription) {
        return subscription != null && !subscription.isUnsubscribed();
    }
}
