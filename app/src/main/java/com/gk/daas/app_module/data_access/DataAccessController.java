package com.gk.daas.app_module.data_access;

/**
 * @author Gabor_Keszthelyi
 */
public interface DataAccessController {

    // Note: Using use-case as parameter here is only to make this PoC implementation simpler.
    void getWeather(UseCase useCase, String city);

    void getForecastForWarmerCity(String city1, String city2);

    void cancelCall();
}
