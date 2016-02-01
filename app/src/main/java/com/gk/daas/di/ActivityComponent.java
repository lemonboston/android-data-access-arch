package com.gk.daas.di;

import com.gk.daas.core.BaseActivity;
import com.gk.daas.screen.main.MainActivity;

import dagger.Component;

/**
 * @author Gabor_Keszthelyi
 */
// TODO move injectors to one static class
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    class Injector {

        public static void inject(MainActivity mainActivity) {
            create(mainActivity).inject(mainActivity);
        }

        private static ActivityComponent create(BaseActivity activity) {
            return DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(activity))
                    .appComponent(AppComponent.Holder.getInstance())
                    .build();
        }
    }
}
