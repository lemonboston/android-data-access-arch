package com.gk.daas.app_module.di;

import com.gk.daas.app_module.screen.main.MainActivity;
import com.gk.daas.app_module.screen.second.SecondActivity;

import dagger.Component;

/**
 * @author Gabor_Keszthelyi
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void satisfy(MainActivity mainActivity);

    void satisfy(SecondActivity mainActivity);

}
