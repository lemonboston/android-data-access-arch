package com.gk.daas.app_module.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Note: State saving solution taken from http://stackoverflow.com/a/8127813/4247460
 *
 * @author Gabor_Keszthelyi
 */
public class KeepVisibilityProgressBar extends ProgressBar {

    private static final String KEY_VISIBILITY = "visibility";
    private static final String KEY_SUPERSTATE = "superState";

    public KeepVisibilityProgressBar(Context context) {
        super(context);
    }

    public KeepVisibilityProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeepVisibilityProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public KeepVisibilityProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPERSTATE, super.onSaveInstanceState());
        bundle.putInt(KEY_VISIBILITY, getVisibility());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            //noinspection ResourceType
            setVisibility(bundle.getInt(KEY_VISIBILITY));
            state = bundle.getParcelable(KEY_SUPERSTATE);
        }
        super.onRestoreInstanceState(state);
    }
}
