package com.gk.daas.data.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabor_Keszthelyi
 */
public class MockData {

    private static final DecimalFormat TWO_DECIMAL_FORMAT = new DecimalFormat("#.##");


    public static WeatherResponse randomWeatherResponse() {
        double randomTemp = 325 + Math.random() * 50;
        randomTemp = twoDecimalsOnly(randomTemp);

        Main main = new Main();
        main.temp = randomTemp;

        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.main = main;

        return weatherResponse;
    }

    public static ForecastResponse randomForecastResponse() {
        ForecastResponse forecastResponse = new ForecastResponse();
        List<WeatherResponse> weatherResponses = new ArrayList<>();
        weatherResponses.add(randomWeatherResponse());
        forecastResponse.list = weatherResponses;
        return forecastResponse;
    }

    private static double twoDecimalsOnly(double temp) {
        return Double.valueOf(TWO_DECIMAL_FORMAT.format(temp));
    }
}
