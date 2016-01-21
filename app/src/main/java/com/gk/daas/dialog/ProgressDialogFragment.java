package com.gk.daas.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * @author Gabor_Keszthelyi
 */
public class ProgressDialogFragment extends DialogFragment {

    private static final String ARG_INITIAL_MESSAGE = "initialMessage";

    public static ProgressDialogFragment newInstance(String initialMessage) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();

        Bundle args = new Bundle();
        args.putString(ARG_INITIAL_MESSAGE, initialMessage);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String initialMessage = getArguments().getString(ARG_INITIAL_MESSAGE);
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(initialMessage);
        return progressDialog;
    }

    public void updateMessage(String message) {
        ((ProgressDialog) getDialog()).setMessage(message);
    }
}
