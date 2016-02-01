package com.gk.daas.di;

import com.gk.daas.bus.Bus;
import com.gk.daas.data.access.DataAccessController;
import com.gk.daas.data.model.converter.ForecastConverter;
import com.gk.daas.data.network.DataAccessControllerImpl;
import com.gk.daas.data.network.ErrorInterpreter;
import com.gk.daas.data.network.OpenWeatherService;
import com.gk.daas.data.network.TaskCounter;
import com.gk.daas.data.network.TaskCounterImpl;
import com.gk.daas.data.network.connection.NetworkConnectionChecker;
import com.gk.daas.data.store.DataStore;
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
    public DataAccessController provideDataAccessController(OpenWeatherService weatherService, LogFactory logFactory, Bus bus, NetworkConnectionChecker connectionChecker, DataStore dataStore, TaskCounter taskCounter, ErrorInterpreter errorInterpreter, ForecastConverter forecastConverter) {
        return new DataAccessControllerImpl(weatherService, logFactory, bus, connectionChecker, dataStore, taskCounter, errorInterpreter, forecastConverter);
    }

    @Provides
    public ForecastConverter provideForecastConverter() {
        return new ForecastConverter();
    }


}
