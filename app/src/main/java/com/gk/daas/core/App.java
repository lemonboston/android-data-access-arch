package com.gk.daas.core;

import android.app.Application;

import com.gk.daas.di.Injector;

/**
 * @author Gabor_Keszthelyi
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.init(this);
    }
}
