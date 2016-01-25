package com.gk.daas.widget;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.gk.daas.screen.ParallelAndChainedCaseFragment;
import com.gk.daas.screen.usecases.basic.BasicCaseFragment;

/**
 * @author Gabor_Keszthelyi
 */
// TODO refactor to make it general
public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BasicCaseFragment();
            case 1:
                return new ParallelAndChainedCaseFragment();
            default:
                throw new IllegalArgumentException("No fragment for position: " + position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
