package com.gk.daas.screen.second;

import android.os.Bundle;
import android.widget.TextView;

import com.gk.daas.R;
import com.gk.daas.bus.Bus;
import com.gk.daas.core.BaseActivity;
import com.gk.daas.data.event.GetTempSuccessEvent;
import com.gk.daas.di.Injector;
import com.gk.daas.util.TemperatureFormatter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        Injector.satisfy(this);
        super.onCreate(savedInstanceState);
        bus.register(this);

        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetTempSuccess(GetTempSuccessEvent event) {
        resultText.setText(event.temperature.toString());
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }
}
