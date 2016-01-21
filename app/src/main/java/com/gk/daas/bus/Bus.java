package com.gk.daas.bus;

/**
 * Event bus for the application.
 *
 * @author Gabor_Keszthelyi
 */
public interface Bus {

    /**
     * Register a subscriber to the event bus.
     *
     * @param subscriber the subscriber
     */
    void register(Object subscriber);

    /**
     * Unregister the given subscriber from the event bus.
     *
     * @param subscriber the subscriber to remove
     */
    void unregister(Object subscriber);

    /**
     * Post an event the the bus.
     *
     * @param event the event
     */
    void post(Object event);

    /**
     * Post a sticky event to a bus, so the last instance of its type will be available for later registered subscribers. (see {@link #registerSticky(Object)}.
     *
     * @param event the event
     */
    void postSticky(Object event);

    /**
     * Removes the sticky event from the bus for the given type.
     *
     * @param eventType the type of the event
     */
    void removeStickyEvent(Class<?> eventType);
}
