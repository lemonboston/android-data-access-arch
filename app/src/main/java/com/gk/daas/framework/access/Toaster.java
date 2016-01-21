package com.gk.daas.framework.access;

import android.widget.Toast;

/**
 * Creates {@link Toast}s.
 *
 * @author Gabor_Keszthelyi
 */
public interface Toaster {

    /**
     * Shows the give message on a Toast for {@link Toast#LENGTH_LONG} period.
     *
     * @param message the message
     */
    void showToast(String message);

}
