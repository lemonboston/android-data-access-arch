package com.gk.daas.data.event;

/**
 * @author Gabor_Keszthelyi
 */
public class GetForecastSuccessEvent {

    public final double lastTemp;

    public GetForecastSuccessEvent(double lastTemp) {
        this.lastTemp = lastTemp;
    }

    @Override
    public String toString() {
        return "GetForecastSuccessEvent{" +
                "lastTemp=" + lastTemp +
                '}';
    }
}
