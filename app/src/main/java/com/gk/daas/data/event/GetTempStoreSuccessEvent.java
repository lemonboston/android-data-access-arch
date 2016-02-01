package com.gk.daas.data.event;

import com.gk.daas.data.model.Temperature;

/**
 * @author Gabor_Keszthelyi
 */
public class GetTempStoreSuccessEvent {

    public final Temperature temperature;

    public GetTempStoreSuccessEvent(Temperature temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "GetTempStoreSuccessEvent{" +
                "temp=" + temperature +
                '}';
    }
}
