package com.gk.daas.framework.listen;

import android.app.Activity;

/**
 * {@link Activity#onDestroy()}  method separated to an interface so the components that this method is delegated to can implement it.
 *
 * @author Gabor_Keszthelyi
 */
public interface OnDestroy {

    /**
     * @see {@link Activity#onDestroy()}.
     */
    void onDestroy();

}
