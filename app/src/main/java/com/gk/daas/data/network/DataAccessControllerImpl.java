package com.gk.daas.data.network;

import com.gk.daas.bus.Bus;
import com.gk.daas.data.access.DataAccessController;
import com.gk.daas.data.event.GetForecastProgressEvent;
import com.gk.daas.data.event.GetForecastSuccessEvent;
import com.gk.daas.data.event.GetTempSuccessEvent;
import com.gk.daas.data.event.RetryEvent;
import com.gk.daas.data.model.client.Forecast;
import com.gk.daas.data.model.client.Temperature;
import com.gk.daas.data.model.converter.ForecastConverter;
import com.gk.daas.data.model.server.WeatherResponse;
import com.gk.daas.data.network.connection.NetworkConnectionChecker;
import com.gk.daas.data.store.DataStore;
import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.gk.daas.data.network.OpenWeatherService.API_KEY;

/**
 * @author Gabor_Keszthelyi
 */
public class DataAccessControllerImpl implements DataAccessController {

    private static final int RETRY_COUNT = 3;
    private static final int RETRY_DELAY_SECONDS = 2;

    private final OpenWeatherService weatherService;
    private final Log log;
    private final Bus bus;
    private final NetworkConnectionChecker connectionChecker;
    private final DataStore dataStore;
    private final TaskCounter taskCounter;
    private final ErrorInterpreter errorInterpreter;
    private final ForecastConverter forecastConverter;

    private Subscription getTempWithOngoingHandlingSubscription;
    private Subscription getTempAllInOneSubscription;

    public DataAccessControllerImpl(OpenWeatherService weatherService, LogFactory logFactory, Bus bus, NetworkConnectionChecker connectionChecker, DataStore dataStore, TaskCounter taskCounter, ErrorInterpreter errorInterpreter, ForecastConverter forecastConverter) {
        this.weatherService = weatherService;
        this.bus = bus;
        this.connectionChecker = connectionChecker;
        this.dataStore = dataStore;
        this.taskCounter = taskCounter;
        this.errorInterpreter = errorInterpreter;
        this.forecastConverter = forecastConverter;
        this.log = logFactory.create(getClass());
    }

    @Override
    public void getWeather(UseCase useCase, String city) {
        switch (useCase) {
            case BASIC:
                getTemperatureBasic(city);
                break;
            case ERROR_HANDLING:
                getTemperatureWithErrorHandling(city);
                break;
            case ONGOING_CALL_HANDLING:
                getTemperatureWithOngoingHandling(city);
                break;
            case OFFLINE_STORAGE:
                getTemperatureWithOfflineLocalStore(city);
                break;
            case RETRY:
                getTemperatureWithRetry(city);
                break;
            case COMBINED:
                getTemperatureCombined(city);
                break;
            case PARALLEL_AND_CHAINED:
                break;
        }
    }

    private void getTemperatureBasic(String city) {
        String tag = "getTemperatureBasic | ";
        log.d(tag + "Starting, city: " + city);

        weatherService.getWeather(city, API_KEY)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        (WeatherResponse weatherResponse) -> {
                            double temp = weatherResponse.main.temp;
                            log.d(tag + "Finished, temp returned: " + temp);
                            bus.post(new GetTempSuccessEvent(new Temperature(temp)));
                            taskCounter.taskFinished();
                        });
    }

    private void getTemperatureWithErrorHandling(String city) {
        String tag = "getTemperatureWithErrorHandling | ";
        log.d(tag + "Starting, city: " + city);

        connectionChecker.checkNetwork()
                .flatMap(aVoid -> weatherService.getWeather(city, "an invalid api key"))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        (WeatherResponse weatherResponse) -> {
                            double temp = weatherResponse.main.temp;
                            log.d(tag + "Finished, temp returned: " + temp);
                            bus.post(new GetTempSuccessEvent(new Temperature(temp)));
                            taskCounter.taskFinished();
                        },
                        throwable -> {
                            DataAccessError dataAccessError = errorInterpreter.interpret(throwable);
                            bus.post(dataAccessError);
                            log.w(tag + "Error: " + throwable);
                            taskCounter.taskFinished();
                        });
    }

    private void getTemperatureWithOngoingHandling(String city) {
        String tag = "getTemperatureWithOngoingHandling |";
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
                            bus.post(new GetTempSuccessEvent(new Temperature(temp)));
                            taskCounter.taskFinished();
                        });
    }

    private void getTemperatureWithOfflineLocalStore(String city) {
        String tag = "getTemperatureWithOfflineLocalStore | ";
        log.d(tag + "Starting, city: " + city);

        weatherService.getWeather(city, API_KEY)

                .doOnSuccess((WeatherResponse weatherResponse) -> {
                    log.d(tag + "Saving temp to data store");
                    dataStore.saveAsync(city, weatherResponse.main.temp);
                })

                .toObservable()
                .onErrorResumeNext(throwable -> {
                    log.w(tag + "Error getting temp through API: " + throwable);
                    return dataStore.getTemperatureAsObservable(city).map(WeatherResponse::createFromTemp);
                })
                .toSingle()

                .subscribeOn(Schedulers.io())

                .subscribe(
                        (WeatherResponse weatherResponse) -> {
                            double temp = weatherResponse.main.temp;
                            log.d(tag + "Temp retrieved (from server or local store), temp: " + temp);
                            bus.post(new GetTempSuccessEvent(new Temperature(temp)));
                            taskCounter.taskFinished();
                        },
                        throwable -> {
                            DataAccessError dataAccessError = errorInterpreter.interpret(throwable);
                            bus.post(dataAccessError);
                            log.w(tag + "Error: " + throwable);
                            taskCounter.taskFinished();
                        });

    }

    private void getTemperatureWithRetry(String city) {
        String tag = "getTemperatureWithRetry | ";
        log.d(tag + "Starting, city: " + city);

        connectionChecker.checkNetwork()
                .flatMap(aVoid -> weatherService.getWeather(city, "invalid api key"))

                .toObservable()
                .retryWhen(retryFunc(tag))
                .toSingle()

                .subscribeOn(Schedulers.io())
                .subscribe(
                        (WeatherResponse weatherResponse) -> {
                            double temp = weatherResponse.main.temp;
                            log.d(tag + "Finished, temp returned: " + temp);
                            bus.post(new GetTempSuccessEvent(new Temperature(temp)));
                            taskCounter.taskFinished();
                        },
                        throwable -> {
                            DataAccessError dataAccessError = errorInterpreter.interpret(throwable);
                            bus.post(dataAccessError);
                            log.w(tag + "Error: " + throwable);
                            taskCounter.taskFinished();
                        });
    }

    // http://reactivex.io/documentation/operators/retry.html
    // Could use this too: http://stackoverflow.com/a/25292833/4247460
    private Func1<Observable<? extends Throwable>, Observable<?>> retryFunc(String tag) {
        return throwableStream ->
                throwableStream
                        .zipWith(Observable.range(1, RETRY_COUNT + 1), (throwable, count) -> {
                            if (count <= RETRY_COUNT) {
                                log.d(tag + "delayed retry #" + count);
                                bus.post(new RetryEvent(count));
                                return null;
                            } else {
                                return throwable;
                            }
                        })
                        .concatMap(nullableThrowable -> nullableThrowable == null ? Observable.timer(RETRY_DELAY_SECONDS, TimeUnit.SECONDS) : Observable.error(nullableThrowable));
    }

    // TODO Case with cancel

    // TODO Case for http cache?

    public void getTemperatureCombined(String city) {
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
                            dataStore.saveAsync(city, weatherResponse.main.temp);
                        })

                        .toObservable()
                        .onErrorResumeNext(throwable -> {
                            log.w(tag + "Error getting temp through API: " + throwable);
                            return dataStore.getTemperatureAsObservable(city).map(WeatherResponse::createFromTemp);
                        })

                        .retryWhen(retryFunc(tag))
                        .toSingle()

                        .subscribeOn(Schedulers.io())

                        .subscribe(
                                (WeatherResponse weatherResponse) -> {
                                    double temp = weatherResponse.main.temp;
                                    log.d(tag + "Temp retrieved (from API or local store), temp: " + temp);
                                    bus.post(new GetTempSuccessEvent(new Temperature(temp)));
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

                .map(forecastConverter::convert)

                .subscribeOn(Schedulers.io())

                .subscribe(
                        (Forecast forecast) -> {
                            log.d(tag + "Finished, forecast:" + forecast);
                            bus.post(new GetForecastSuccessEvent(forecast));
                            bus.postSticky(GetForecastProgressEvent.COMPLETED);
                            taskCounter.taskFinished();
                        },
                        throwable -> {
                            bus.postSticky(GetForecastProgressEvent.COMPLETED);
                            DataAccessError dataAccessError = errorInterpreter.interpret(throwable);
                            bus.post(dataAccessError);
                            log.w(tag + "Error: " + throwable);
                            taskCounter.taskFinished();
                        }
                );

    }

    private boolean isOngoing(Subscription subscription) {
        return subscription != null && !subscription.isUnsubscribed();
    }
}
