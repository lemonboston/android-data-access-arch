package com.gk.daas.data.access;

/**
 * @author Gabor_Keszthelyi
 */
public interface DataAccessController {

    void getTemperature_basic(String city);

    void getTemperature_wErrorHandling(String city);

    void getTemperature_wOngoingHandling(String city);

    void getTemperature_wOfflineLocalStore(String city);

    void getTemperature_allInOne(String city);

    void getForecastForWarmerCity(String city1, String city2);
}
