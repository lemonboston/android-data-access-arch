package com.gk.daas.app_module.model;

import com.gk.daas.app_module.core.Config;

import static com.gk.daas.app_module.util.TemperatureFormatter.convertAndFormatToCelsius;
import static com.gk.daas.app_module.util.TemperatureFormatter.convertAndFormatToFahrenheit;

/**
 * @author Gabor_Keszthelyi
 */
public class Temperature {

    public final double temperatureInKelvin;

    public Temperature(double temperatureInKelvin) {
        this.temperatureInKelvin = temperatureInKelvin;
    }

    @Override
    public String toString() {
        return Config.USE_FAHRENHEIT ? convertAndFormatToFahrenheit(temperatureInKelvin) : convertAndFormatToCelsius(temperatureInKelvin);
    }
}
