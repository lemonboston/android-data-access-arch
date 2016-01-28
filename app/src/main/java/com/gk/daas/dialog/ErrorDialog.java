package com.gk.daas.dialog;

import android.app.AlertDialog;
import android.content.Context;

import com.gk.daas.R;

/**
 * @author Gabor_Keszthelyi
 */
public class ErrorDialog {

    private final Context context;

    public ErrorDialog(Context context) {
        this.context = context;
    }

    public void show(String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.dismiss, null)
                .show();

    }
}
