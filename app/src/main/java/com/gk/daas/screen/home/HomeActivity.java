package com.gk.daas.screen.home;


import android.os.Bundle;

import com.gk.daas.core.BaseActivity;
import com.gk.daas.di.ActivityComponent;

import javax.inject.Inject;


public class HomeActivity extends BaseActivity {

    @Inject
    HomeController homeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityComponent.Injector.inject(this);
        super.onCreate(savedInstanceState);
        homeController.onCreate();
    }

    @Override
    protected void onDestroy() {
        homeController.onDestroy();
        super.onDestroy();
    }

}
