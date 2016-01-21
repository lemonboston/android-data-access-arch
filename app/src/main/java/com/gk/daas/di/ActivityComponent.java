package com.gk.daas.di;

import com.gk.daas.core.BaseActivity;
import com.gk.daas.screen.home.HomeActivity;

import dagger.Component;

/**
 * @author Gabor_Keszthelyi
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(HomeActivity activity);

    class Injector {

        public static void inject(HomeActivity homeActivity) {
            create(homeActivity).inject(homeActivity);
        }

        private static ActivityComponent create(BaseActivity activity) {
            return DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(activity))
                    .appComponent(AppComponent.Holder.getInstance())
                    .build();
        }
    }
}
