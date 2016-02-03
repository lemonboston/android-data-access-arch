package com.gk.daas.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.gk.daas.R;
import com.gk.daas.databinding.MockRealServiceSelectorBinding;
import com.gk.daas.di.DebugOptions;

/**
 * @author Gabor_Keszthelyi
 */
public class ServiceSelectorDialog {

    private final Context context;

    private Dialog dialog;

    public ServiceSelectorDialog(Context context) {
        this.context = context;
    }

    public void show() {
        MockRealServiceSelectorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.mock_real_service_selector, null, false);
        if (DebugOptions.MOCK_WEATHER_SERVICE) {
            binding.MockService.setChecked(true);
        } else {
            binding.RealService.setChecked(true);
        }
        binding.MockService.setOnClickListener(v -> setMockServiceAndDismiss(true, dialog));
        binding.RealService.setOnClickListener(v -> setMockServiceAndDismiss(false, dialog));

        dialog = new Dialog(context);
        dialog.setContentView(binding.getRoot());
        dialog.show();
    }

    private void setMockServiceAndDismiss(boolean isMockService, Dialog dialog) {
        DebugOptions.MOCK_WEATHER_SERVICE = isMockService;
        dialog.dismiss();
    }

}
