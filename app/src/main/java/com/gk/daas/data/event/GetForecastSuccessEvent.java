package com.gk.daas.data.event;

/**
 * @author Gabor_Keszthelyi
 */
public class GetForecastSuccessEvent {

    public final double lastTemp;
    public final String cityName;

    public GetForecastSuccessEvent(double lastTemp, String cityName) {
        this.lastTemp = lastTemp;
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "GetForecastSuccessEvent{" +
                "lastTemp=" + lastTemp +
                '}';
    }
}
