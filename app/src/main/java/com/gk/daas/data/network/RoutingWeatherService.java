package com.gk.daas.data.network;

import com.gk.daas.core.Config;
import com.gk.daas.data.model.server.ForecastResponse;
import com.gk.daas.data.model.server.WeatherResponse;

import retrofit2.http.Query;
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
    public Single<WeatherResponse> getWeather(@Query("q") String queryString, @Query("appid") String apiKey) {
        if (Config.MOCK_WEATHER_SERVICE) {
            return mockService.getWeather(queryString, apiKey);
        } else {
            return realService.getWeather(queryString, apiKey);
        }
    }

    @Override
    public Single<ForecastResponse> getForecast(@Query("q") String queryString, @Query("appid") String apiKey) {
        if (Config.MOCK_WEATHER_SERVICE) {
            return mockService.getForecast(queryString, apiKey);
        } else {
            return realService.getForecast(queryString, apiKey);
        }
    }
}
