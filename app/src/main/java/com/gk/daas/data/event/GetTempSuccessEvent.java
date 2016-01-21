package com.gk.daas.data.event;

/**
 * @author Gabor_Keszthelyi
 */
public class GetTempSuccessEvent {

    public final double temp;

    public GetTempSuccessEvent(double temp) {
        this.temp = temp;
    }


    @Override
    public String toString() {
        return "GetTempSuccessEvent{" +
                "temp=" + temp +
                '}';
    }
}
