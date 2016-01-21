package com.gk.daas.data.event;

/**
 * @author Gabor_Keszthelyi
 */
public class GetForecastForWarmestCitySuccessEvent {

    public final double lastTemp;

    public GetForecastForWarmestCitySuccessEvent(double lastTemp) {
        this.lastTemp = lastTemp;
    }

    @Override
    public String toString() {
        return "GetForecastForWarmestCitySuccessEvent{" +
                "lastTemp=" + lastTemp +
                '}';
    }
}
