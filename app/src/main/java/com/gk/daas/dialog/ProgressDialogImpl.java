package com.gk.daas.dialog;

import android.app.FragmentManager;

/**
 * @author Gabor_Keszthelyi
 */
public class ProgressDialogImpl implements ProgressDialog {

    private static final String TAG_PROGRESS_DIALOG = "progressDialog_tag";
    private final FragmentManager fragmentManager;

    public ProgressDialogImpl(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void showMessage(String message) {
        ProgressDialogFragment dialogFragment = findFragment();
        if (dialogFragment == null) {
            ProgressDialogFragment newDialogFragment = new ProgressDialogFragment();
            newDialogFragment.setMessage(message);
            newDialogFragment.show(fragmentManager, TAG_PROGRESS_DIALOG);
        } else {
            dialogFragment.updateMessage(message);
        }
    }

    @Override
    public void dismiss() {
        ProgressDialogFragment dialogFragment = findFragment();
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    private ProgressDialogFragment findFragment() {
        return (ProgressDialogFragment) fragmentManager.findFragmentByTag(TAG_PROGRESS_DIALOG);
    }
}
