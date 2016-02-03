package com.gk.daas.di;

import com.gk.daas.bus.Bus;
import com.gk.daas.data.access.DataAccessInitiator;
import com.gk.daas.data.network.ErrorInterpreter;
import com.gk.daas.data.network.NetworkServiceIntentHelper;
import com.gk.daas.data.network.OpenWeatherService;
import com.gk.daas.data.network.SyncSchedulerBroadcastReceiver;
import com.gk.daas.data.network.connection.NetworkConnectionChecker;
import com.gk.daas.data.store.DataStore;
import com.gk.daas.framework.access.StringResAccess;
import com.gk.daas.framework.access.Toaster;
import com.gk.daas.log.LogFactory;
import com.gk.daas.screen.second.SecondActivity;
import com.gk.daas.util.TemperatureFormatter;

import javax.inject.Named;
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

    void satisfy(SecondActivity secondActivity);

    void satisfy(SyncSchedulerBroadcastReceiver syncSchedulerBroadcastReceiver);

    LogFactory getLogFactory();

    DataAccessInitiator getDataAccess();

    NetworkServiceIntentHelper getNetworkServiceIntentHelper();

    Bus getBus();

    TemperatureFormatter getTemperatureFormatter();

    Toaster getToaster();

    @Named(Name.ROUTER)
    OpenWeatherService getOpenWeatherService();

    NetworkConnectionChecker getNetworkConnectionChecker();

    DataStore getDataStore();

    StringResAccess getStringResAccess();

    ErrorInterpreter getErrorInterpreter();

}
