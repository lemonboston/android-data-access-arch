package com.gk.daas.core;

/**
 * @author Gabor_Keszthelyi
 */
public class Config {

    public static class Cache {

        public static final String CACHE_SUB_DIR = "responses";
        public static final int CACHE_MAX_SIZE = 5 * 1024 * 1024; // 5 MB
    }
}
