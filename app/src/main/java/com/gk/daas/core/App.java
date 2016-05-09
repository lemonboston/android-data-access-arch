package com.gk.daas.core;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.gk.daas.di.Injector;

/**
 * @author Gabor_Keszthelyi
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Injector.init(this);

        if (Config.ENABLE_STETHO) {
            Stetho.initializeWithDefaults(this);
        }
    }
}
