package com.gk.daas.screen.second;

import android.os.Bundle;
import android.widget.TextView;


import com.gk.daas.R;
import com.gk.daas.bus.Bus;
import com.gk.daas.core.BaseActivity;
import com.gk.daas.data.event.GetTempSuccessEvent;
import com.gk.daas.di.AppComponent;
import com.gk.daas.util.TemperatureFormatter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * @author Gabor_Keszthelyi
 */
public class SecondActivity extends BaseActivity {

    @Bind(R.id.ResultText)
    TextView resultText;

    @Inject
    Bus bus;

    @Inject
    TemperatureFormatter temperatureFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppComponent.Holder.getInstance().inject(this);
        super.onCreate(savedInstanceState);
        bus.register(this);

        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGetTempSuccess(GetTempSuccessEvent event) {
        resultText.setText(temperatureFormatter.formatTempInKelvin(event.temp));
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }
}
