package com.gk.daas.data.access;

/**
 * @author Gabor_Keszthelyi
 */
public interface DataAccessController {

    void getTemperature(String city);

    void getForecastForWarmestCity(String city1, String city2);

    void getTemperatureDiff(String city1, String city2);

    void getTemperature_OfflineLocalStore(String city);
}
