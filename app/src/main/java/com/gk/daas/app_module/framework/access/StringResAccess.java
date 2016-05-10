package com.gk.daas.app_module.framework.access;

import android.support.annotation.StringRes;

/**
 * Accesses string resources.
 *
 * @author Gabor_Keszthelyi
 */
public interface StringResAccess {

    /**
     * @see android.content.Context#getString(int)
     */
    String getString(@StringRes int resId);

    /**
     * @see android.content.Context#getString(int, Object...)
     */
    String getString(@StringRes int resId, Object... formatArgs);
}
