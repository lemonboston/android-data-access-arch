package com.gk.daas.di;

import com.gk.daas.data.network.NetworkService;

import dagger.Component;

/**
 * @author Gabor_Keszthelyi
 */
@ServiceScope
@Component(dependencies = AppComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

    void inject(NetworkService networkService);

    class Injector {

        public static void inject(NetworkService networkService) {
            DaggerServiceComponent.builder()
                    .appComponent(AppComponent.Holder.getInstance())
                    .serviceModule(new ServiceModule())
                    .build()
                    .inject(networkService);
        }
    }
}
