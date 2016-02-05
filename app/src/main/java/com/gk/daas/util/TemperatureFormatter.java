package com.gk.daas.util;

/**
 * @author Gabor_Keszthelyi
 */
public class TemperatureFormatter {

    private static final double ZERO_CELSIUS_IN_KELVIN = 273.15;
    private static final String DEGREE_CELSIUS = "\u00b0" + "C";

    public static String formatTempInKelvinToCelsius(double temp) {
        return formatTempToCelsius(temp - ZERO_CELSIUS_IN_KELVIN);
    }

    private static String formatTempToCelsius(double tempInCelsius) {
        return String.format("%.1f\u00A0%s", tempInCelsius, DEGREE_CELSIUS);
    }
}
