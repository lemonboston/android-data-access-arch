package com.gk.daas.screen.usecases.basic;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
// TODO add go to other screen button
// TODO input text for city
// TODO error handling?
public class BasicCaseFragment extends Fragment {

    @Inject
    DataAccessInitiator dataAccessInitiator;

    @Inject
    Bus bus;

    @Inject
    TemperatureFormatter temperatureFormatter;

    @Bind(R.id.ResultText)
    TextView resultTextView;

    @Bind(R.id.ProgressBar)
    ProgressBar progressBar;

    @Bind(R.id.Button_Execute)
    Button executeButton;

    @Bind(R.id.Text_WeatherUseCase)
    TextView weatherUseCase;

    @Bind(R.id.TechnicalUseCase_Description)
    TextView technicalUseCase;

    @Bind(R.id.ImplementationDescription)
    TextView implementationDescription;

    @Bind(R.id.EditText_City1)
    EditText cityText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_use_case, container, false);
        ButterKnife.bind(this, view);

        executeButton.setText(R.string.GetTemp_Button);
        weatherUseCase.setText(R.string.WeatherUseCase_GetTemp);
        technicalUseCase.setText(R.string.UseCase_Basic_Description);
        implementationDescription.setText(R.string.UseCase_Basic_Implementation);

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
        dataAccessInitiator.getTemperature(cityText.getText().toString());
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

