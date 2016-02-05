package com.gk.daas.util;

/**
 * @author Gabor_Keszthelyi
 */
public class TemperatureFormatter {

    private static final String DEGREE = "\u00b0";
    private static final String DEGREE_CELSIUS = DEGREE + "C";
    private static final String DEGREE_FAHRENHEIT = DEGREE + "F";

    public static String convertAndFormatToCelsius(double tempInKelvin) {
        return formatWithDegreeSign(kelvinToCelsius(tempInKelvin), DEGREE_CELSIUS);
    }

    public static String convertAndFormatToFahrenheit(double tempInKelvin) {
        return formatWithDegreeSign(kelvinToFahrenheit(tempInKelvin), DEGREE_FAHRENHEIT);
    }

    private static double kelvinToCelsius(double tempInKelvin) {
        return tempInKelvin - 273.15;
    }

    private static double kelvinToFahrenheit(double tempInKelvin) {
        return tempInKelvin * 9 / 5 - 459.67;
    }

    private static String formatWithDegreeSign(double temp, String degreeSign) {
        return String.format("%.1f\u00A0%s", temp, degreeSign);
    }
}
