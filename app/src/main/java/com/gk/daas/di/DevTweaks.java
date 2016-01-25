package com.gk.daas.di;


import com.gk.daas.BuildConfig;

/**
 * @author Gabor_Keszthelyi
 */
public class DevTweaks {

    public static boolean MOCK_WEATHER_SERVICE;

    static {

        if (BuildConfig.DEBUG) {
            MOCK_WEATHER_SERVICE = true;


        } else { // DON'T TOUCH, FOR RELEASE
            MOCK_WEATHER_SERVICE = false;


        }
    }
}

