package com.gk.daas.framework.listen;

import android.app.Activity;
import android.os.Bundle;

/**
 * {@link Activity#onCreate(Bundle)} method separated to an interface so the components that this method is delegated to can implement it.
 *
 * @author Gabor_Keszthelyi
 */
public interface OnCreate {

    /**
     * @see {@link Activity#onCreate(Bundle)}
     */
    void onCreate();

}
