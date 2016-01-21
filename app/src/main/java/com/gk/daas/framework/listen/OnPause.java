package com.gk.daas.framework.listen;

import android.app.Activity;

/**
 * {@link Activity#onPause()} method separated to an interface so the components that this method is delegated to can implement it.
 *
 * @author robert_nandor_juhasz
 */
public interface OnPause {

    /**
     * {@link Activity#onPause()} callback.
     */
    void onPause();
}
