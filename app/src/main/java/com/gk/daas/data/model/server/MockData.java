package com.gk.daas.data.model.server;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabor_Keszthelyi
 */
public class MockData {

    private static final DecimalFormat TWO_DECIMAL_FORMAT = new DecimalFormat("#.##");
    public static final String INVALID_API_KEY = "an invalid api key";

    public static WeatherResponse randomWeatherResponse() {
        double randomTemp = 325 + Math.random() * 50;
        randomTemp = twoDecimalsOnly(randomTemp);

        return weatherResponse(randomTemp);
    }

    private static WeatherResponse weatherResponse(double randomTemp) {
        Main main = new Main();
        main.temp = randomTemp;

        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.main = main;

        return weatherResponse;
    }

    public static ForecastResponse randomForecastResponse() {
        ForecastResponse forecastResponse = new ForecastResponse();

        City city = new City();
        city.name = "San Francisco";
        forecastResponse.city = city;

        List<WeatherResponse> weatherResponses = new ArrayList<>();
        weatherResponses.add(randomWeatherResponse());
        forecastResponse.list = weatherResponses;
        return forecastResponse;
    }

    private static double twoDecimalsOnly(double temp) {
        return Double.valueOf(TWO_DECIMAL_FORMAT.format(temp));
    }

    public static ForecastResponse forecastResponse(String city, double temp) {
        ForecastResponse forecastResponse = new ForecastResponse();

        City cityObj = new City();
        cityObj.name = city;
        forecastResponse.city = cityObj;

        List<WeatherResponse> weatherResponses = new ArrayList<>();
        weatherResponses.add(weatherResponse(temp));
        forecastResponse.list = weatherResponses;
        return forecastResponse;
    }
}
