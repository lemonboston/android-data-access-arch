package com.gk.daas.data.access;

import com.gk.daas.data.network.UseCase;

/**
 * @author Gabor_Keszthelyi
 */
public interface DataAccessController {

    // Note: Using use-case as parameter here is only to make this PoC implementation simpler.
    void getWeather(UseCase useCase, String city);

    void getForecastForWarmerCity(String city1, String city2);

    void cancelCall();
}
