package com.gk.daas.data.model.client;

import com.gk.daas.core.Config;

import static com.gk.daas.util.TemperatureFormatter.convertAndFormatToCelsius;
import static com.gk.daas.util.TemperatureFormatter.convertAndFormatToFahrenheit;

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
        return Config.USE_FAHRENHEIT ? convertAndFormatToFahrenheit(temperatureInKelvin) : convertAndFormatToCelsius(temperatureInKelvin);
    }
}
