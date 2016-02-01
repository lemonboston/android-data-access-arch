package com.gk.daas.data.model.client;

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

    public String getCity() {
        return city;
    }

    public Temperature getTemperature() {
        return temperature;
    }


    @Override
    public String toString() {
        return city + ": " + temperature.toString();
    }
}
