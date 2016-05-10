package com.gk.daas.app_module.log;

/**
 * Abstraction for logging to enable any kind of customized behaviour, additional actions. (eg: just log to logcat, or to analytics as well, disable logging, etc)
 * <p>
 * There are no TAG parameters for the methods to simplify client usage. TAG can be passed in to the constructor of the implementations.
 *
 * @author Gabor_Keszthelyi
 */
public interface Log {

    /**
     * A prefix for the log tags. Adding this can help filtering the logs.
     */
    String APP_TAG_PREFIX = "NETWORK_POC_";

    /**
     * VERBOSE level logging.
     */
    void v(String msg);

    /**
     * DEBUG level logging.
     */
    void d(String msg);

    /**
     * INFO level logging.
     */
    void i(String msg);

    /**
     * WARNING level logging.
     */
    void w(String msg);

    /**
     * ERROR level logging.
     */
    void e(String msg);

    /**
     * ERROR level logging.
     */
    void e(String msg, Throwable cause);
}
