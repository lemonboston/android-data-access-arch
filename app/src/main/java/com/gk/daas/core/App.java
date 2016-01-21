package com.gk.daas.core;

import android.app.Application;

import com.gk.daas.di.AppComponent;

/**
 * @author Gabor_Keszthelyi
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent.Holder.init(this);
    }
}
