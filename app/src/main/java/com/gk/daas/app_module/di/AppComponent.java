package com.gk.daas.app_module.di;

import com.gk.daas.app_module.bus.Bus;
import com.gk.daas.app_module.data_access.DataAccessController;
import com.gk.daas.app_module.data_access.connection.NetworkConnectionChecker;
import com.gk.daas.app_module.data_access.error.ErrorInterpreter;
import com.gk.daas.app_module.data_access.store.DataStore;
import com.gk.daas.app_module.framework.access.StringResAccess;
import com.gk.daas.app_module.framework.access.Toaster;
import com.gk.daas.app_module.log.LogFactory;
import com.gk.daas.app_module.screen.second.SecondActivity;
import com.gk.daas.app_module.sync.SyncScheduler;
import com.gk.daas.app_module.sync.SyncSchedulerBroadcastReceiver;
import com.gk.daas.network_module.di.NetworkModule;
import com.gk.daas.network_module.service.OpenWeatherService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Application (global) scope dagger Component.
 *
 * @author Gabor_Keszthelyi
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    void satisfy(SecondActivity secondActivity);

    void satisfy(SyncSchedulerBroadcastReceiver syncSchedulerBroadcastReceiver);

    LogFactory getLogFactory();

    DataAccessController getDataAccessController();

    Bus getBus();

    Toaster getToaster();

    @Named(Name.ROUTER)
    OpenWeatherService getOpenWeatherService();

    NetworkConnectionChecker getNetworkConnectionChecker();

    DataStore getDataStore();

    StringResAccess getStringResAccess();

    ErrorInterpreter getErrorInterpreter();

    SyncScheduler getSyncScheduler();

}
