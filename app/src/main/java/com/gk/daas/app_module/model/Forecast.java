package com.gk.daas.app_module.model;

/**
 * @author Gabor_Keszthelyi
 */
public class Forecast {

    private final String city;
    private final Temperature temperature;

    public Forecast(String city, Temperature temperature) {
        this.city = city;
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return city + ": " + temperature.toString();
    }
}
