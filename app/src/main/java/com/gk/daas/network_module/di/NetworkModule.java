package com.gk.daas.network_module.di;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gk.daas.app_module.di.Name;
import com.gk.daas.app_module.log.LogFactory;
import com.gk.daas.network_module.service.MockWeatherService;
import com.gk.daas.network_module.service.OpenWeatherService;
import com.gk.daas.network_module.service.RoutingWeatherService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * @author Gabor_Keszthelyi
 */
@Singleton
@Module
public class NetworkModule {

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
}
