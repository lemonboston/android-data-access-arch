package com.gk.daas.data.model.client;

import com.gk.daas.util.TemperatureFormatter;

/**
 * @author Gabor_Keszthelyi
 */
public class Temperature {

    private final double temperatureInKelvin;

    public Temperature(double temperatureInKelvin) {
        this.temperatureInKelvin = temperatureInKelvin;
    }

    @Override
    public String toString() {
        return TemperatureFormatter.formatTempInKelvinToCelsius(temperatureInKelvin);
    }
}
