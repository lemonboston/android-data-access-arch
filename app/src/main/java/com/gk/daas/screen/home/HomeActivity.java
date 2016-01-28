package com.gk.daas.screen.home;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gk.daas.R;
import com.gk.daas.core.BaseActivity;
import com.gk.daas.di.ActivityComponent;
import com.gk.daas.di.DebugOptions;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Menu_RealService:
                DebugOptions.MOCK_WEATHER_SERVICE = false;
                restartActivity();
                return true;
            case R.id.Menu_MockService:
                DebugOptions.MOCK_WEATHER_SERVICE = true;
                restartActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void restartActivity() {
        finish();
        startActivity(getIntent());
    }
}
