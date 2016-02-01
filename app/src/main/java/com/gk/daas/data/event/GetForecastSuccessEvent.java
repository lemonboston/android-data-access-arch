package com.gk.daas.data.event;

import com.gk.daas.data.model.client.Forecast;

/**
 * @author Gabor_Keszthelyi
 */
public class GetForecastSuccessEvent {

    public final Forecast forecast;

    public GetForecastSuccessEvent(Forecast forecast) {
        this.forecast = forecast;
    }

    @Override
    public String toString() {
        return "GetForecastSuccessEvent{" +
                "forecast=" + forecast +
                '}';
    }
}
