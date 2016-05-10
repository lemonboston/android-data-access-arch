package com.gk.daas.app_module.data_access.event;

import com.gk.daas.app_module.model.Temperature;

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
