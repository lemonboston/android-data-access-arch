package com.gk.daas.data.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;
import com.google.gson.Gson;

import rx.Single;
import rx.SingleSubscriber;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

/**
 * @author Gabor_Keszthelyi
 */
public class DataStoreImpl implements DataStore {

    private final SharedPreferences prefs;
    private final Gson gson;
    private final Log log;

    public DataStoreImpl(Context context, Gson gson, LogFactory logFactory) {
        this.prefs = context.getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        this.gson = gson;
        this.log = logFactory.create(getClass());
    }

    @Override
    public <T> void save(Key<T> key, T data) {
        String json = gson.toJson(data);
        log.d(String.format("Saving json. Key: %s | Type: %s | JSON: %s", key.name, key.dataClass.getSimpleName(), json));
        prefs.edit().putString(key.name, json).apply();
    }

    @Override
    public <T> void saveAsync(Key<T> key, T data) {
        Async.toAsync(() -> save(key, data), Schedulers.io());
    }

    @Override
    public <T> T get(Key<T> key) {
        String json = prefs.getString(key.name, null);
        log.d(String.format("Retrieving json. Key: %s | Type: %s | JSON: %s", key.name, key.dataClass.getSimpleName(), json));
        return gson.fromJson(json, key.dataClass);
    }

    @Override
    public <T> Single<T> getAsSingle(Key<T> key) {
        return Single.create(new Single.OnSubscribe<T>() {
            @Override
            public void call(SingleSubscriber<? super T> singleSubscriber) {
                T value = get(key);
                if (value != null) {
                    singleSubscriber.onSuccess(value);
                } else {
                    singleSubscriber.onError(new NoOfflineDataException());
                }
            }
        });
    }
}
