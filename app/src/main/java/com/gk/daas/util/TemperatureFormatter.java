package com.gk.daas.util;

/**
 * @author Gabor_Keszthelyi
 */
public class TemperatureFormatter {

    private static final double ZERO_CELSIUS_IN_KELVIN = 273.15;
    private static final String DEGREE = "\u00b0" + "C";

    public String formatTempInKelvin(double temp) {
        return String.format("%.1f %s", temp - ZERO_CELSIUS_IN_KELVIN, DEGREE);
    }

    public String formatTempDiff(double tempDiff) {
        return String.format("%.1f %s", tempDiff, DEGREE);
    }
}
