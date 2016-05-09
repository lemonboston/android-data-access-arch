package com.gk.daas.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.gk.daas.R;
import com.gk.daas.core.Config;
import com.gk.daas.framework.access.Toaster;

/**
 * @author Gabor_Keszthelyi
 */
public class ServiceSelectorDialog {

    private final Context context;
    private final Toaster toaster;

    public ServiceSelectorDialog(Context context, Toaster toaster) {
        this.context = context;
        this.toaster = toaster;
    }

    public void show() {
        Dialog dialog = new Dialog(context);

        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_service_selector, null);
        RadioButton mockServiceButton = (RadioButton) layout.findViewById(R.id.MockService);
        RadioButton realServiceButton = (RadioButton) layout.findViewById(R.id.RealService);

        if (Config.MOCK_WEATHER_SERVICE) {
            mockServiceButton.setChecked(true);
        } else {
            realServiceButton.setChecked(true);
        }
        mockServiceButton.setOnClickListener(v -> setMockServiceAndDismiss(true, dialog));
        realServiceButton.setOnClickListener(v -> setMockServiceAndDismiss(false, dialog));

        dialog.setContentView(layout);
        dialog.show();
    }

    private void setMockServiceAndDismiss(boolean isMockService, Dialog dialog) {
        Config.MOCK_WEATHER_SERVICE = isMockService;
        dialog.dismiss();
        toaster.showToast(isMockService ? R.string.SelectorDialog_MockSelected : R.string.SelectorDialog_RealSelected);
    }

}
