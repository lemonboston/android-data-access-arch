package com.gk.daas.network_module.data;

/**
 * @author Gabor_Keszthelyi
 */
public class WeatherResponse {

    public Main main;

    public static WeatherResponse createFromTemp(double temp) {
        Main main = new Main();
        main.temp = temp;
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.main = main;
        return weatherResponse;
    }
}
