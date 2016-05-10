package com.gk.daas.network_module.service;

import com.gk.daas.app_module.core.Config;
import com.gk.daas.app_module.log.Log;
import com.gk.daas.app_module.log.LogFactory;
import com.gk.daas.network_module.data.ForecastResponse;
import com.gk.daas.network_module.data.MockData;
import com.gk.daas.network_module.data.WeatherResponse;

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
    public Single<WeatherResponse> getWeather(String queryString, String apiKey) {
        return Single.create(new Single.OnSubscribe<WeatherResponse>() {
            @Override
            public void call(SingleSubscriber<? super WeatherResponse> singleSubscriber) {
                sleep(Config.MOCK_SERVICE_RESPONSE_DELAY);
                log.d("Mock weather call finish");
                singleSubscriber.onSuccess(MockData.randomWeatherResponse());
            }
        });
    }

    @Override
    public Single<ForecastResponse> getForecast(String queryString, String apiKey) {
        return Single.create(new Single.OnSubscribe<ForecastResponse>() {
            @Override
            public void call(SingleSubscriber<? super ForecastResponse> singleSubscriber) {
                sleep(Config.MOCK_SERVICE_RESPONSE_DELAY);
                log.d("Mock forecast call finish");
                singleSubscriber.onSuccess(MockData.randomForecastResponse());

            }
        });
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.w("Sleep interrupted");
        }
    }
}
