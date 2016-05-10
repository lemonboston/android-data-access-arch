package com.gk.daas.app_module.di;

import com.gk.daas.app_module.core.App;
import com.gk.daas.app_module.core.BaseActivity;
import com.gk.daas.app_module.screen.main.MainActivity;
import com.gk.daas.app_module.screen.second.SecondActivity;
import com.gk.daas.app_module.sync.SyncSchedulerBroadcastReceiver;

/**
 * @author Gabor_Keszthelyi
 */
public class Injector {

    private static class AppComponentHolder {

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

    public static void init(App app) {
        AppComponentHolder.init(app);
    }

    public static void satisfy(MainActivity mainActivity) {
        createActivityComponent(mainActivity).satisfy(mainActivity);
    }

    public static void satisfy(SecondActivity secondActivity) {
        createActivityComponent(secondActivity).satisfy(secondActivity);
    }

    public static void satisfy(SyncSchedulerBroadcastReceiver syncSchedulerBroadcastReceiver) {
        AppComponentHolder.getInstance().satisfy(syncSchedulerBroadcastReceiver);
    }

    private static ActivityComponent createActivityComponent(BaseActivity activity) {
        return DaggerActivityComponent.builder()
                .appComponent(AppComponentHolder.getInstance())
                .activityModule(new ActivityModule(activity))
                .build();
    }
}
