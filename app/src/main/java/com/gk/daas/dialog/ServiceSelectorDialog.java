package com.gk.daas.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.RadioButton;

import com.gk.daas.R;
import com.gk.daas.di.DebugOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Gabor_Keszthelyi
 */
public class ServiceSelectorDialog {

    private final Activity activity;

    @Bind(R.id.Radio_RealService)
    RadioButton realServiceButton;

    @Bind(R.id.Radio_MockService)
    RadioButton mockServiceButton;

    private Dialog dialog;

    public ServiceSelectorDialog(Activity activity) {
        this.activity = activity;
    }

    public void show() {
        View layout = activity.getLayoutInflater().inflate(R.layout.mock_real_service_selector, null);
        ButterKnife.bind(this, layout);

        dialog = new Dialog(activity);
        dialog.setContentView(layout);

        if (DebugOptions.MOCK_WEATHER_SERVICE) {
            mockServiceButton.setChecked(true);
        } else {
            realServiceButton.setChecked(true);
        }

        dialog.show();
    }

    @OnClick(R.id.Radio_RealService)
    void onRealServiceRadioButtonClick() {
        DebugOptions.MOCK_WEATHER_SERVICE = false;
        dialog.dismiss();
    }

    @OnClick(R.id.Radio_MockService)
    void onMockServiceRadioButtonClick() {
        DebugOptions.MOCK_WEATHER_SERVICE = true;
        dialog.dismiss();
    }

    // TODO create proxy service that decides dynamically runtime, so no need to restart activity
    private void restartActivity() {
        activity.finish();
        activity.startActivity(activity.getIntent());
    }
}
