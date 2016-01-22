package com.gk.daas.di;

import com.gk.daas.core.BaseActivity;
import com.gk.daas.screen.home.HomeActivity;
import com.gk.daas.screen.usecases.basic.BasicCaseFragment;

import dagger.Component;

/**
 * @author Gabor_Keszthelyi
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(HomeActivity activity);

    void inject(BasicCaseFragment basicCaseFragment);

    class Injector {

        public static void inject(HomeActivity homeActivity) {
            create(homeActivity).inject(homeActivity);
        }

        public static void inject(BasicCaseFragment basicCaseFragment) {
            create((BaseActivity) basicCaseFragment.getActivity()).inject(basicCaseFragment);
        }

        private static ActivityComponent create(BaseActivity activity) {
            return DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(activity))
                    .appComponent(AppComponent.Holder.getInstance())
                    .build();
        }
    }
}
