package com.gk.daas.app_module.bus;

import com.gk.daas.app_module.log.Log;
import com.gk.daas.app_module.log.LogFactory;

import org.greenrobot.eventbus.EventBus;


/**
 * The implementation of {@link Bus}.
 *
 * @author Gabor_Keszthelyi
 */
public class BusImpl implements Bus {

    private final EventBus eventBus;
    private final Log log;

    public BusImpl(EventBus eventBus, LogFactory logFactory) {
        this.eventBus = eventBus;
        this.log = logFactory.create(getClass());
    }

    @Override
    public void register(Object subscriber) {
        log.v("Register subscriber: " + subscriber);
        eventBus.register(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        log.v("Unregister subscriber: " + subscriber);
        eventBus.unregister(subscriber);
    }

    @Override
    public void post(Object event) {
        log.v("Post event: " + event);
        eventBus.post(event);
    }

    @Override
    public void postSticky(Object event) {
        log.v("Post sticky event: " + event);
        eventBus.postSticky(event);
    }

    @Override
    public void removeStickyEvent(Class<?> eventType) {
        log.v("Remove sticky event: " + eventType);
        eventBus.removeStickyEvent(eventType);
    }

}
