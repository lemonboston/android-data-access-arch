package com.gk.daas.di;

import android.net.ConnectivityManager;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gk.daas.bus.Bus;
import com.gk.daas.bus.BusImpl;
import com.gk.daas.core.App;
import com.gk.daas.data.access.DataAccessController;
import com.gk.daas.data.model.converter.ForecastConverter;
import com.gk.daas.data.network.DataAccessControllerImpl;
import com.gk.daas.data.network.ErrorInterpreter;
import com.gk.daas.data.network.ErrorInterpreterImpl;
import com.gk.daas.data.network.MockWeatherService;
import com.gk.daas.data.network.OpenWeatherService;
import com.gk.daas.data.network.RoutingWeatherService;
import com.gk.daas.data.network.SyncScheduler;
import com.gk.daas.data.network.SyncSchedulerImpl;
import com.gk.daas.data.network.connection.NetworkConnectionChecker;
import com.gk.daas.data.store.DataStore;
import com.gk.daas.data.store.DataStoreImpl;
import com.gk.daas.framework.access.StringResAccess;
import com.gk.daas.framework.access.StringResAccessImpl;
import com.gk.daas.framework.access.Toaster;
import com.gk.daas.framework.access.ToasterImpl;
import com.gk.daas.log.LogFactory;
import com.gk.daas.log.LogFactoryImpl;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
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

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        /** Uncomment below for HTTP Cache simulation **/

        //         File httpCacheDirectory = new File(app.getCacheDir(), Config.Cache.CACHE_SUB_DIR);
        //         Cache cache = new Cache(httpCacheDirectory, Config.Cache.CACHE_MAX_SIZE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logger)
                .addNetworkInterceptor(new StethoInterceptor())

                        //                        TODO maybe try add to config the max-age string so it could be changed dynamically
                        // https://github.com/square/okhttp/wiki/Interceptors
                        // http://stackoverflow.com/questions/23429046/can-retrofit-with-okhttp-use-cache-data-when-offline
                        //                         .addNetworkInterceptor((Interceptor.Chain chain) -> {
                        //                             Response response = chain.proceed(chain.request());
                        //                             return response.newBuilder()
                        //                                     .header("Cache-Control", "private, max-age=20")
                        //                                     .build();
                        //                         })
                        //                                        .cache(cache)
                .build();

        return client;
    }

    @Provides
    @Named(Name.ROUTER)
    OpenWeatherService provideRouterWeatherService(@Named(Name.REAL) OpenWeatherService realService, @Named(Name.MOCK) OpenWeatherService mockService) {
        return new RoutingWeatherService(realService, mockService);
    }

    @Provides
    @Named(Name.MOCK)
    OpenWeatherService provideMockWeatherService(LogFactory logFactory) {
        return new MockWeatherService(logFactory);
    }

    @Provides
    @Named(Name.REAL)
    OpenWeatherService provideRealWeatherService(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OpenWeatherService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(OpenWeatherService.class);
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
