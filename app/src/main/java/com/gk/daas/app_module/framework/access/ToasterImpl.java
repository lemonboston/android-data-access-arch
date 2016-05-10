package com.gk.daas.app_module.framework.access;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * Default implementation of {@link Toaster}.
 *
 * @author Gabor_Keszthelyi
 */
public class ToasterImpl implements Toaster {

    private final Context context;
    private final Handler uiHandler;

    @Inject
    public ToasterImpl(Context context) {
        this.context = context;
        uiHandler = new Handler(context.getMainLooper());
    }

    @Override
    public void showToast(String message) {
        uiHandler.post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showToast(@StringRes int messageResId) {
        showToast(context.getString(messageResId));
    }

}
