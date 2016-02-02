package com.gk.daas.data.event;

/**
 * @author Gabor_Keszthelyi
 */
public class RetryEvent {
    public final Integer retryCount;

    public RetryEvent(Integer retryCount) {
        this.retryCount = retryCount;
    }
}
