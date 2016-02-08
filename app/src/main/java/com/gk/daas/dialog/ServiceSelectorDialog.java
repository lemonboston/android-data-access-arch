package com.gk.daas.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.gk.daas.R;
import com.gk.daas.core.Config;
import com.gk.daas.databinding.DialogServiceSelectorBinding;
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

        DialogServiceSelectorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_service_selector, null, false);
        if (Config.MOCK_WEATHER_SERVICE) {
            binding.MockService.setChecked(true);
        } else {
            binding.RealService.setChecked(true);
        }
        binding.MockService.setOnClickListener(v -> setMockServiceAndDismiss(true, dialog));
        binding.RealService.setOnClickListener(v -> setMockServiceAndDismiss(false, dialog));

        dialog.setContentView(binding.getRoot());
        dialog.show();
    }

    private void setMockServiceAndDismiss(boolean isMockService, Dialog dialog) {
        Config.MOCK_WEATHER_SERVICE = isMockService;
        dialog.dismiss();
        toaster.showToast(isMockService ? R.string.SelectorDialog_MockSelected : R.string.SelectorDialog_RealSelected);
    }

}
