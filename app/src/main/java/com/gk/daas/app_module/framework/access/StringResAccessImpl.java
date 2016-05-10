package com.gk.daas.app_module.framework.access;

import android.content.Context;
import android.support.annotation.StringRes;

import javax.inject.Inject;

/**
 * Default implementation of {@link StringResAccess}
 *
 * @author Gabor_Keszthelyi
 */
public class StringResAccessImpl implements StringResAccess {

    private final Context context;

    @Inject
    public StringResAccessImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getString(@StringRes int resId) {
        return context.getString(resId);
    }

    @Override
    public String getString(@StringRes int resId, Object... formatArgs) {
        return context.getString(resId, formatArgs);
    }
}
