package com.gk.daas.data.network;

/**
 * @author Gabor_Keszthelyi
 */
public interface NetworkController {

    void getTemp(String city);

    void getForecastForWarmestCity(String city1, String city2);

    void getTemperatureDiff(String city1, String city2);

    void getTemp_OfflineLocalStore(String city);
}
