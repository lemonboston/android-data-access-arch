package com.gk.daas.app_module.data_access.event;

/**
 * @author Gabor_Keszthelyi
 */
public class RetryEvent {

    public final Integer retryCount;

    public RetryEvent(Integer retryCount) {
        this.retryCount = retryCount;
    }


    @Override
    public String toString() {
        return "RetryEvent{" +
                "retryCount=" + retryCount +
                '}';
    }
}
