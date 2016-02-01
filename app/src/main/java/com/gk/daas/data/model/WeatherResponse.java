package com.gk.daas.data.model;

/**
 * @author Gabor_Keszthelyi
 */
public class WeatherResponse {

    public static WeatherResponse createFromTemp(double temp) {
        Main main = new Main();
        main.temp = temp;
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.main = main;
        return weatherResponse;
    }

    public Main main;
}
