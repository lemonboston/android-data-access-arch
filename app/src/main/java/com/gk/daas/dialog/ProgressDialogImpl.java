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
    public void show(String message) {
        ProgressDialogFragment dialog = ProgressDialogFragment.newInstance(message);
        fragmentManager.beginTransaction().add(dialog, TAG_PROGRESS_DIALOG).commit();
    }

    @Override
    public void updateMessage(String message) {
        ProgressDialogFragment dialogFragment = findIt();
        if (dialogFragment != null) {
            dialogFragment.updateMessage(message);
        }
    }

    @Override
    public void dismiss() {
        ProgressDialogFragment dialogFragment = findIt();
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    private ProgressDialogFragment findIt() {
        return (ProgressDialogFragment) fragmentManager.findFragmentByTag(TAG_PROGRESS_DIALOG);
    }
}
