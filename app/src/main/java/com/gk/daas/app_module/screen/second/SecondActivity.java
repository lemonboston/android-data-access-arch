package com.gk.daas.app_module.screen.second;

import android.os.Bundle;
import android.widget.TextView;

import com.gk.daas.R;
import com.gk.daas.app_module.bus.Bus;
import com.gk.daas.app_module.core.BaseActivity;
import com.gk.daas.app_module.data_access.event.GetTempSuccessEvent;
import com.gk.daas.app_module.di.Injector;

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
