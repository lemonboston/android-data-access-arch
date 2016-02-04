package com.gk.daas.core;

/**
 * @author Gabor_Keszthelyi
 */
public class Config {

    public static boolean MOCK_WEATHER_SERVICE = false;

    public static final int BACKGROUND_SERVICE_SHUTDOWN_TIMEOUT_SECONDS = 60;
    public static final int RETRY_COUNT = 3;

    public static final int RETRY_DELAY_SECONDS = 2;

    public static class Cache {

        public static final String CACHE_SUB_DIR = "responses";
        public static final int CACHE_MAX_SIZE = 5 * 1024 * 1024; // 5 MB
    }
}
