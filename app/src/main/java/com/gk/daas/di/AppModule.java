package com.gk.daas.di;

import android.net.ConnectivityManager;

import com.gk.daas.bus.Bus;
import com.gk.daas.bus.BusImpl;
import com.gk.daas.core.App;
import com.gk.daas.core.Config;
import com.gk.daas.data.access.DataAccessInitiator;
import com.gk.daas.data.access.DataAccessInitiatorImpl;
import com.gk.daas.data.network.ErrorInterpreter;
import com.gk.daas.data.network.ErrorInterpreterImpl;
import com.gk.daas.data.network.MockWeatherService;
import com.gk.daas.data.network.NetworkServiceIntentHelper;
import com.gk.daas.data.network.OpenWeatherService;
import com.gk.daas.data.network.SyncScheduler;
import com.gk.daas.data.network.connection.NetworkConnectionChecker;
import com.gk.daas.data.store.DataStore;
import com.gk.daas.data.store.DataStoreImpl;
import com.gk.daas.framework.access.StringResAccess;
import com.gk.daas.framework.access.StringResAccessImpl;
import com.gk.daas.framework.access.Toaster;
import com.gk.daas.framework.access.ToasterImpl;
import com.gk.daas.log.LogFactory;
import com.gk.daas.log.LogFactoryImpl;
import com.gk.daas.util.TemperatureFormatter;
import com.google.gson.Gson;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * @author Gabor_Keszthelyi
 */
@Module
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    public DataAccessInitiator provideDataAccess(NetworkServiceIntentHelper intentHelper) {
        return new DataAccessInitiatorImpl(app, intentHelper);
    }

    @Provides
    public NetworkServiceIntentHelper provideNetworkServiceIntentHelper() {
        return new NetworkServiceIntentHelper();
    }

    @Provides
    public NetworkConnectionChecker provideNetworkConnectionChecker() {
        return new NetworkConnectionChecker(app.getSystemService(ConnectivityManager.class));
    }

    @Provides
    public DataStore provideDataStore(Gson gson, LogFactory logFactory) {
        return new DataStoreImpl(app, gson, logFactory);
    }

    @Provides
    public Gson provideGson() {
        return new Gson();
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
    public OpenWeatherService provideWeatherService(LogFactory logFactory, OkHttpClient okHttpClient) {
        if (DevTweaks.MOCK_WEATHER_SERVICE) {
            return new MockWeatherService(logFactory);
        } else {
            return createWeatherService(okHttpClient);
        }
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        File httpCacheDirectory = new File(app.getCacheDir(), Config.Cache.CACHE_SUB_DIR);
        Cache cache = new Cache(httpCacheDirectory, Config.Cache.CACHE_MAX_SIZE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logger)

                        // https://github.com/square/okhttp/wiki/Interceptors
                        // http://stackoverflow.com/questions/23429046/can-retrofit-with-okhttp-use-cache-data-when-offline
                .addNetworkInterceptor((Interceptor.Chain chain) -> {
                    Response response = chain.proceed(chain.request());
                    return response.newBuilder()
                            .header("Cache-Control", "private, max-age=20")
                            .build();

                })
                .cache(cache)
                .build();

        return client;
    }

    private OpenWeatherService createWeatherService(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OpenWeatherService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(OpenWeatherService.class);
    }

    @Provides
    public Bus provideBus(LogFactory logFactory) {
        return new BusImpl(EventBus.getDefault(), logFactory);
    }

    @Provides
    public SyncScheduler provideSyncScheduler(LogFactory logFactory) {
        return new SyncScheduler(app, logFactory);
    }

    @Provides
    public TemperatureFormatter provideTemperatureFormatter() {
        return new TemperatureFormatter();
    }

    @Provides
    public StringResAccess provideStringResAccess() {
        return new StringResAccessImpl(app);
    }

    @Provides
    public ErrorInterpreter provideErrorInterpreter(LogFactory logFactory) {
        return new ErrorInterpreterImpl(logFactory);
    }

}
