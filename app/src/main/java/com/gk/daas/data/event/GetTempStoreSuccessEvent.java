package com.gk.daas.data.event;

/**
 * @author Gabor_Keszthelyi
 */
public class GetTempStoreSuccessEvent {

    public final double temp;

    public GetTempStoreSuccessEvent(double temp) {
        this.temp = temp;
    }


    @Override
    public String toString() {
        return "GetTempStoreSuccessEvent{" +
                "temp=" + temp +
                '}';
    }
}
