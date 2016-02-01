package com.gk.daas.data.store;

import rx.Observable;
import rx.Single;

/**
 * Very simple and specific data store for saving last temperature for a city just for the PoC. Real solutions would be much more generic.
 *
 * @author Gabor_Keszthelyi
 */
public interface DataStore {

    void save(String city, double temperature);

    void saveAsync(String city, double temperature);

    Double getTemperature(String city);

    Single<Double> getTemperatureAsSingle(String city);

    Observable<Double> getTemperatureAsObservable(String city);

    void clearStore();

}
