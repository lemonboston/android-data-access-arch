package com.gk.daas.data.event;

import com.gk.daas.data.model.client.Temperature;

/**
 * @author Gabor_Keszthelyi
 */
public class GetTempSuccessEvent {

    public final Temperature temperature;

    public GetTempSuccessEvent(Temperature temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "GetTempSuccessEvent{" +
                "temperature=" + temperature +
                '}';
    }
}
