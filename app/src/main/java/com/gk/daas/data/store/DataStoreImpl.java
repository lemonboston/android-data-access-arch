package com.gk.daas.data.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;
import com.google.gson.Gson;

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
    public <T> T get(Key<T> key) {
        String json = prefs.getString(key.name, null);
        log.d(String.format("Retrieving json. Key: %s | Type: %s | JSON: %s", key.name, key.dataClass.getSimpleName(), json));
        return gson.fromJson(json, key.dataClass);
    }
}
