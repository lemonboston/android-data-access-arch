package com.gk.daas.framework.access;

import android.app.Activity;
import android.support.v4.view.ViewPager;

/**
 * @author Gabor_Keszthelyi
 */
public class ViewFactoryImpl implements ViewFactory {

    private final Activity context;

    public ViewFactoryImpl(Activity context) {
        this.context = context;
    }

    @Override
    public ViewPager newViewPager() {
        return new ViewPager(context);
    }
}
