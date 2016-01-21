package com.gk.daas.framework.listen;

import android.app.Activity;

/**
 * {@link Activity#onResume()} method separated to an interface so the components that this method is delegated to can implement it.
 *
 * @author Gabor_Keszthelyi
 */
public interface OnResume {

    /**
     * @see {@link Activity#onResume()}.
     */
    void onResume();
}
