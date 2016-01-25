package com.gk.daas.dialog;

/**
 * @author Gabor_Keszthelyi
 */
// TODO with the sticky events, fragment may not be needed.. just dialog, check
public interface ProgressDialog {

    void showMessage(String message);

    void dismiss();
}
