package com.gk.daas.screen.home;


import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.gk.daas.R;
import com.gk.daas.core.BaseActivity;
import com.gk.daas.di.ActivityComponent;
import com.gk.daas.widget.HomeFragmentPagerAdapter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HomeActivity extends BaseActivity {

    @Bind(R.id.ViewPager)
    ViewPager viewPager;

    @Inject
    HomeFragmentPagerAdapter homeFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityComponent.Injector.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        viewPager.setAdapter(homeFragmentPagerAdapter);
    }

}
