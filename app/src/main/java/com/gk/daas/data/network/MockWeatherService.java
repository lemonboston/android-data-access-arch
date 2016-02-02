package com.gk.daas.data.network;

import com.gk.daas.data.model.server.ForecastResponse;
import com.gk.daas.data.model.server.MockData;
import com.gk.daas.data.model.server.WeatherResponse;
import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

import retrofit2.http.Query;
import rx.Single;
import rx.SingleSubscriber;

/**
 * @author Gabor_Keszthelyi
 */
public class MockWeatherService implements OpenWeatherService {

    private final Log log;

    public MockWeatherService(LogFactory logFactory) {
        this.log = logFactory.create(getClass());
    }

    @Override
    public Single<WeatherResponse> getWeather(@Query("q") String queryString, @Query("appid") String apiKey) {
        return Single.create(new Single.OnSubscribe<WeatherResponse>() {
            @Override
            public void call(SingleSubscriber<? super WeatherResponse> singleSubscriber) {
                sleep(3000);
                log.d("Mock weather call finish");
                singleSubscriber.onSuccess(MockData.randomWeatherResponse());
            }
        });
    }

    @Override
    public Single<ForecastResponse> getForecast(@Query("q") String queryString, @Query("appid") String apiKey) {
        return Single.create(new Single.OnSubscribe<ForecastResponse>() {
            @Override
            public void call(SingleSubscriber<? super ForecastResponse> singleSubscriber) {
                sleep(4000);
                log.d("Mock forecast call finish");
                singleSubscriber.onSuccess(MockData.randomForecastResponse());

            }
        });
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.e("Sleep interrupted", e);
        }
    }
}
