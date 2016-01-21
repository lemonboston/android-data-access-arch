package com.gk.daas.data.event;

/**
 * @author Gabor_Keszthelyi
 */
public class GetDiffSuccessEvent {

    public final Double tempDiff;

    public GetDiffSuccessEvent(Double tempDiff) {
        this.tempDiff = tempDiff;
    }

    @Override
    public String toString() {
        return "GetDiffSuccessEvent{" +
                "tempDiff=" + tempDiff +
                '}';
    }
}
