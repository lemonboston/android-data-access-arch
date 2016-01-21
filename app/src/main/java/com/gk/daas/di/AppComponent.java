package com.gk.daas.di;

import com.gk.daas.bus.Bus;
import com.gk.daas.core.App;
import com.gk.daas.data.access.DataAccessInitiator;
import com.gk.daas.data.network.ErrorInterpreter;
import com.gk.daas.data.network.NetworkServiceIntentHelper;
import com.gk.daas.data.network.OpenWeatherService;
import com.gk.daas.data.network.SyncScheduler;
import com.gk.daas.data.network.SyncSchedulerBroadcastReceiver;
import com.gk.daas.data.network.connection.NetworkConnectionChecker;
import com.gk.daas.data.store.DataStore;
import com.gk.daas.framework.access.StringResAccess;
import com.gk.daas.framework.access.Toaster;
import com.gk.daas.log.LogFactory;
import com.gk.daas.screen.second.SecondActivity;
import com.gk.daas.util.TemperatureFormatter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Application (global) scope dagger Component.
 *
 * @author Gabor_Keszthelyi
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(SecondActivity secondActivity);

    void inject(SyncSchedulerBroadcastReceiver syncSchedulerBroadcastReceiver);

    LogFactory getLogFactory();

    DataAccessInitiator getDataAccess();

    SyncScheduler getSyncScheduler();

    NetworkServiceIntentHelper getNetworkServiceIntentHelper();

    Bus getBus();

    TemperatureFormatter getTemperatureFormatter();

    Toaster getToaster();

    OpenWeatherService getOpenWeatherService();

    NetworkConnectionChecker getNetworkConnectionChecker();

    DataStore getDataStore();

    StringResAccess getStringResAccess();

    ErrorInterpreter getErrorInterpreter();

    class Holder {

        private static AppComponent instance;

        public static void init(App app) {
            instance = DaggerAppComponent
                    .builder()
                    .appModule(new AppModule(app))
                    .build();
        }

        public static AppComponent getInstance() {
            if (instance == null) {
                throw new RuntimeException(AppComponent.class.getSimpleName() + " has not been initialized.");
            }
            return instance;
        }

    }
}
