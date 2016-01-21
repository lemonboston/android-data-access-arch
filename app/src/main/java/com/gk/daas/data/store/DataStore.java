package com.gk.daas.data.store;

import com.gk.daas.data.model.WeatherResponse;

/**
 * @author Gabor_Keszthelyi
 */
public interface DataStore {

    <T> void save(Key<T> key, T data);

    <T> T get(Key<T> key);

    class Key<T> {
        public final String name;
        public final Class<T> dataClass;

        public Key(String name, Class<T> dataClass) {
            this.name = name;
            this.dataClass = dataClass;
        }
    }

    Key<WeatherResponse> GET_TEMP = new Key<>("GetTemp", WeatherResponse.class);
    Key<Double> GET_TEMP_DIFF = new Key<>("GetTempDiff", Double.class);
    Key<Double> GET_FORECAST = new Key<>("GetForecast", Double.class);

}
