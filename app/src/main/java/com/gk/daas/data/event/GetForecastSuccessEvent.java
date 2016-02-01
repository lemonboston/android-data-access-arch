package com.gk.daas.data.event;

import com.gk.daas.data.model.Temperature;

/**
 * @author Gabor_Keszthelyi
 */
public class GetForecastSuccessEvent {

    public final Temperature lastTemp;
    public final String cityName;

    public GetForecastSuccessEvent(Temperature lastTemp, String cityName) {
        this.lastTemp = lastTemp;
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "GetForecastSuccessEvent{" +
                "lastTemp=" + lastTemp +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
