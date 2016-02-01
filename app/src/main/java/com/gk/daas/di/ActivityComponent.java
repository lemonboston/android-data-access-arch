package com.gk.daas.di;

import com.gk.daas.screen.main.MainActivity;
import com.gk.daas.screen.second.SecondActivity;

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
