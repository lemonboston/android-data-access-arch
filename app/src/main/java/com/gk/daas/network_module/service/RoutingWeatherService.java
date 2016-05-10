package com.gk.daas.network_module.service;

import com.gk.daas.app_module.core.Config;
import com.gk.daas.network_module.data.ForecastResponse;
import com.gk.daas.network_module.data.WeatherResponse;

import rx.Single;

/**
 * @author Gabor_Keszthelyi
 */
public class RoutingWeatherService implements OpenWeatherService {

    private final OpenWeatherService realService;
    private final OpenWeatherService mockService;

    public RoutingWeatherService(OpenWeatherService realService, OpenWeatherService mockService) {
        this.realService = realService;
        this.mockService = mockService;
    }

    @Override
    public Single<WeatherResponse> getWeather(String queryString, String apiKey) {
        if (Config.MOCK_WEATHER_SERVICE) {
            return mockService.getWeather(queryString, apiKey);
        } else {
            return realService.getWeather(queryString, apiKey);
        }
    }

    @Override
    public Single<ForecastResponse> getForecast(String queryString, String apiKey) {
        if (Config.MOCK_WEATHER_SERVICE) {
            return mockService.getForecast(queryString, apiKey);
        } else {
            return realService.getForecast(queryString, apiKey);
        }
    }
}
