package com.gk.daas.app_module.di;

import android.net.ConnectivityManager;

import com.gk.daas.app_module.bus.Bus;
import com.gk.daas.app_module.bus.BusImpl;
import com.gk.daas.app_module.core.App;
import com.gk.daas.app_module.data_access.DataAccessController;
import com.gk.daas.app_module.data_access.DataAccessControllerImpl;
import com.gk.daas.app_module.data_access.connection.NetworkConnectionChecker;
import com.gk.daas.app_module.data_access.error.ErrorInterpreter;
import com.gk.daas.app_module.data_access.error.ErrorInterpreterImpl;
import com.gk.daas.app_module.data_access.store.DataStore;
import com.gk.daas.app_module.data_access.store.DataStoreImpl;
import com.gk.daas.app_module.framework.access.StringResAccess;
import com.gk.daas.app_module.framework.access.StringResAccessImpl;
import com.gk.daas.app_module.framework.access.Toaster;
import com.gk.daas.app_module.framework.access.ToasterImpl;
import com.gk.daas.app_module.log.LogFactory;
import com.gk.daas.app_module.log.LogFactoryImpl;
import com.gk.daas.app_module.model.converter.ForecastConverter;
import com.gk.daas.app_module.sync.SyncScheduler;
import com.gk.daas.app_module.sync.SyncSchedulerImpl;
import com.gk.daas.network_module.service.OpenWeatherService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

/**
 * @author Gabor_Keszthelyi
 */
@Module
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Singleton
    @Provides
    DataAccessController provideDataAccessController(@Named(Name.ROUTER) OpenWeatherService weatherService, LogFactory logFactory, Bus bus, NetworkConnectionChecker connectionChecker, DataStore dataStore, ErrorInterpreter errorInterpreter, ForecastConverter forecastConverter) {
        return new DataAccessControllerImpl(weatherService, logFactory, bus, connectionChecker, dataStore, errorInterpreter, forecastConverter, Schedulers.io());
    }

    @Provides
    NetworkConnectionChecker provideNetworkConnectionChecker() {
        return new NetworkConnectionChecker(app.getSystemService(ConnectivityManager.class));
    }

    @Provides
    DataStore provideDataStore(LogFactory logFactory, Toaster toaster) {
        return new DataStoreImpl(app, logFactory, toaster);
    }

    @Provides
    ForecastConverter provideForecastConverter() {
        return new ForecastConverter();
    }

    @Provides
    Toaster provideToaster() {
        return new ToasterImpl(app);
    }

    @Singleton
    @Provides
    LogFactory provideLogFactory() {
        return new LogFactoryImpl();
    }


    @Provides
    Bus provideBus(LogFactory logFactory) {
        return new BusImpl(EventBus.getDefault(), logFactory);
    }

    @Provides
    SyncScheduler provideSyncScheduler(LogFactory logFactory, Toaster toaster) {
        return new SyncSchedulerImpl(app, logFactory, toaster);
    }

    @Provides
    StringResAccess provideStringResAccess() {
        return new StringResAccessImpl(app);
    }

    @Provides
    ErrorInterpreter provideErrorInterpreter(LogFactory logFactory) {
        return new ErrorInterpreterImpl(logFactory);
    }

}
