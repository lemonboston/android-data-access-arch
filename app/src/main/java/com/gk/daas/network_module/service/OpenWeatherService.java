package com.gk.daas.network_module.service;

import com.gk.daas.network_module.data.ForecastResponse;
import com.gk.daas.network_module.data.WeatherResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

/**
 * @author Gabor_Keszthelyi
 */
public interface OpenWeatherService {

    String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    String API_KEY = "6ca33d477972e07209965cc09f76a533";

    @GET("weather")
    Single<WeatherResponse> getWeather(@Query("q") String queryString, @Query("appid") String apiKey);

    @GET("forecast")
    Single<ForecastResponse> getForecast(@Query("q") String queryString, @Query("appid") String apiKey);
}
