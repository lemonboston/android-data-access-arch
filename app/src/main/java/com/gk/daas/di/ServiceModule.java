package com.gk.daas.di;

import com.gk.daas.bus.Bus;
import com.gk.daas.data.network.ErrorInterpreter;
import com.gk.daas.data.network.NetworkController;
import com.gk.daas.data.network.NetworkControllerImpl;
import com.gk.daas.data.network.OpenWeatherService;
import com.gk.daas.data.network.TaskCounter;
import com.gk.daas.data.network.TaskCounterImpl;
import com.gk.daas.data.network.connection.NetworkConnectionChecker;
import com.gk.daas.data.store.DataStore;
import com.gk.daas.framework.access.Toaster;
import com.gk.daas.log.LogFactory;

import dagger.Module;
import dagger.Provides;

/**
 * @author Gabor_Keszthelyi
 */
@ServiceScope
@Module
public class ServiceModule {

    @ServiceScope
    @Provides
    TaskCounter provideTaskCounter(LogFactory logFactory) {
        return new TaskCounterImpl(logFactory);
    }

    @Provides
    public NetworkController provideNetworkController(OpenWeatherService weatherService, LogFactory logFactory, Bus bus, Toaster toaster, NetworkConnectionChecker connectionChecker, DataStore dataStore, TaskCounter taskCounter, ErrorInterpreter errorInterpreter) {
        return new NetworkControllerImpl(weatherService, logFactory, bus, toaster, connectionChecker, dataStore, taskCounter, errorInterpreter);
    }


}
