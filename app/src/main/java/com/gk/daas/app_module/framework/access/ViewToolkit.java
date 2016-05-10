package com.gk.daas.app_module.framework.access;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Provides the framework functions that a View component may need.
 *
 * @author Gabor_Keszthelyi
 */
public interface ViewToolkit {

    /**
     * @see Activity#setContentView(int)
     */
    void setContentView(@LayoutRes int layoutResID);

    /**
     * @see Activity#setContentView(View)
     */
    void setContentView(View view);

    /**
     * @see {@link Activity#findViewById(int)}.
     */
    View findViewById(int id);

    /**
     * Injects Views and OnClickListeners using ButterKnife.
     *
     * @param target object with the annotated fields and methods
     */
    void injectViews(Object target);

}
