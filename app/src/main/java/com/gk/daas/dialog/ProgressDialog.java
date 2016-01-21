package com.gk.daas.dialog;

/**
 * @author Gabor_Keszthelyi
 */
public interface ProgressDialog {

    void show(String message);

    void updateMessage(String message);

    void dismiss();
}
