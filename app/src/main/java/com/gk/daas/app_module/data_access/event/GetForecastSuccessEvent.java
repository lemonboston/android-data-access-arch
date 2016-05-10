package com.gk.daas.app_module.data_access.event;

import com.gk.daas.app_module.model.Forecast;

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
