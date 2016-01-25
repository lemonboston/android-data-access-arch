package com.gk.daas.di;

import com.gk.daas.core.BaseActivity;
import com.gk.daas.screen.ParallelAndChainedCaseFragment;
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

    void inject(ParallelAndChainedCaseFragment parallelAndChainedCaseFragment);

    class Injector {

        public static void inject(HomeActivity homeActivity) {
            create(homeActivity).inject(homeActivity);
        }

        public static void inject(BasicCaseFragment basicCaseFragment) {
            create((BaseActivity) basicCaseFragment.getActivity()).inject(basicCaseFragment);
        }

        public static void inject(ParallelAndChainedCaseFragment fragment) {
            create((BaseActivity) fragment.getActivity()).inject(fragment);
        }

        private static ActivityComponent create(BaseActivity activity) {
            return DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(activity))
                    .appComponent(AppComponent.Holder.getInstance())
                    .build();
        }
    }
}
