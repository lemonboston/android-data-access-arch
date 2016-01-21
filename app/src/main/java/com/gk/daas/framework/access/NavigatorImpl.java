package com.gk.daas.framework.access;

import android.app.Activity;
import android.content.Intent;

/**
 * Default implementation of {@link Navigator}.
 *
 * @author robert_nandor_juhasz
 */
public class NavigatorImpl implements Navigator {
    private final Activity activity;

    public NavigatorImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void startActivity(Intent intent) {
        activity.startActivity(intent);
    }

    @Override
    public void startActivity(Class<? extends Activity> clazz) {
        activity.startActivity(new Intent(activity, clazz));
    }

    @Override
    public void startActivityAndFinishCurrent(Class<? extends Activity> clazz) {
        startActivity(clazz);
        activity.finish();
    }

    @Override
    public void startActivityWithoutTransitionAndFinishCurrent(Class<? extends Activity> clazz) {
        startActivityAndFinishCurrent(clazz);
        activity.overridePendingTransition(0, 0);
    }

    @Override
    public void finishThisActivity() {
        activity.finish();
    }

    @Override
    public void reloadThisActivity() {
        activity.finish();
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition(0, 0);
    }
}
