package com.gk.daas.framework.access;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.gk.daas.core.BaseActivity;

import butterknife.ButterKnife;

/**
 * Default implementation of {@link ViewToolkit}.
 *
 * @author Gabor_Keszthelyi
 */
public class ViewToolkitImpl implements ViewToolkit {

    private final BaseActivity activity;

    public ViewToolkitImpl(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        activity.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        activity.setContentView(view);
    }

    @Override
    public View findViewById(int id) {
        return activity.findViewById(id);
    }

    @Override
    public void injectViews(Object target) {
        ButterKnife.bind(target, activity);
    }

}
