package com.gk.daas.screen.usecases.basic;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gk.daas.R;
import com.gk.daas.bus.Bus;
import com.gk.daas.data.access.DataAccessInitiator;
import com.gk.daas.data.event.GetTempSuccessEvent;
import com.gk.daas.di.ActivityComponent;
import com.gk.daas.util.TemperatureFormatter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * @author Gabor_Keszthelyi
 */
public class BasicCaseFragment extends Fragment {

    @Inject
    DataAccessInitiator dataAccessInitiator;

    @Inject
    Bus bus;

    @Inject
    TemperatureFormatter temperatureFormatter;

    @Bind(R.id.GetTemp_ResultText)
    TextView resultTextView;

    @Bind(R.id.ProgressBar)
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_case, container, false);
        ButterKnife.bind(this, view);
        ActivityComponent.Injector.inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bus.register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bus.unregister(this);
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.Button_Execute)
    void onExecuteButtonClick() {
        progressBar.setVisibility(View.VISIBLE);
        dataAccessInitiator.getTemperature("Budapest");
    }

    @OnClick(R.id.Button_Clear)
    void onClearButtonClick() {
        resultTextView.setText(null);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGetTempSuccess(GetTempSuccessEvent event) {
        progressBar.setVisibility(View.GONE);
        String temperature = temperatureFormatter.formatTempInKelvin(event.temp);
        resultTextView.setText(temperature);
    }
}

