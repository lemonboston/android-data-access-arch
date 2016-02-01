package com.gk.daas.data.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.SingleSubscriber;
import rx.schedulers.Schedulers;

/**
 * Uses SharedPreferences just for the sake of simplicity of the PoC.
 *
 * @author Gabor_Keszthelyi
 */
public class DataStoreImpl implements DataStore {

    private final SharedPreferences prefs;
    private final Log log;

    public DataStoreImpl(Context context, LogFactory logFactory) {
        this.prefs = context.getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        this.log = logFactory.create(getClass());
    }

    @Override
    public void save(String city, double temperature) {
        log.d(String.format("Saving temperature. City: %s | Temperature: %s", city, temperature));
        prefs.edit().putFloat(city, (float) temperature).apply();
    }

    @Override
    public void saveAsync(String city, double temperature) {
        Scheduler.Worker worker = Schedulers.io().createWorker();
        worker.schedule(() -> {
            save(city, temperature);
            worker.unsubscribe();
        });
    }

    @Override
    public Double getTemperature(String city) {
        float temperature = prefs.getFloat(city, 0);
        log.d(String.format("Retrieving saved temperature.City: %s | Temperature: %s", city, temperature));
        return (double) temperature;
    }

    @Override
    public Single<Double> getTemperatureAsSingle(String city) {
        return Single.create(new Single.OnSubscribe<Double>() {
            @Override
            public void call(SingleSubscriber<? super Double> singleSubscriber) {
                Double value = getTemperature(city);
                if (value != 0) {
                    singleSubscriber.onSuccess(value);
                } else {
                    singleSubscriber.onError(new NoOfflineDataException());
                }
            }
        });
    }

    @Override
    public Observable<Double> getTemperatureAsObservable(String city) {
        return getTemperatureAsSingle(city).toObservable();
    }

    @Override
    public void clearStore() {
        prefs.edit().clear().apply();
    }
}
