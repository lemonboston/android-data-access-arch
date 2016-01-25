package com.gk.daas.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * @author Gabor_Keszthelyi
 */
public class ProgressDialogFragment extends DialogFragment {

    private String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        setCancelable(false);
        super.onCreate(savedInstanceState);
    }

    // To prevent dialog disappearing on rotation. http://stackoverflow.com/a/14016339/4247460
    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void updateMessage(String message) {
        this.message = message;
        ((ProgressDialog) getDialog()).setMessage(message);
    }
}
