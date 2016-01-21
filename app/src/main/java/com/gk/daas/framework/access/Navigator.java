package com.gk.daas.framework.access;

import android.app.Activity;
import android.content.Intent;

/**
 * This interface is responsible for in-app navigation.
 *
 * @author robert_nandor_juhasz
 */
public interface Navigator {

    /**
     * Start an activity.
     *
     * @param intent the intent.
     */
    void startActivity(Intent intent);

    /**
     * Start an activity.
     *
     * @param clazz The class of the given activity to start.
     */
    void startActivity(Class<? extends Activity> clazz);

    /**
     * Start an activity and call {@link Activity#finish()} on the current one.
     *
     * @param clazz The class of the given activity to start.
     */
    void startActivityAndFinishCurrent(Class<? extends Activity> clazz);

    /**
     * Starts the activity with the given class without transition (overridePendingTransition(0,0) and finishes current activity.
     *
     * @param clazz the class of the activity to start
     */
    void startActivityWithoutTransitionAndFinishCurrent(Class<? extends Activity> clazz);

    /**
     * Finishes the current Activity.
     */
    void finishThisActivity();

    /**
     * Reloads the current Activity without transition.
     */
    void reloadThisActivity();

}
