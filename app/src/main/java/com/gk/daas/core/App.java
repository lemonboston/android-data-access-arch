package com.gk.daas.core;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.frogermcs.dagger2metrics.Dagger2Metrics;
import com.gk.daas.di.Injector;

/**
 * @author Gabor_Keszthelyi
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Config.ENABLE_DAGGER2METRICS) {
            Dagger2Metrics.enableCapturing(this);
        }

        Injector.init(this);

        if (Config.ENABLE_STETHO) {
            Stetho.initializeWithDefaults(this);
        }
    }
}
