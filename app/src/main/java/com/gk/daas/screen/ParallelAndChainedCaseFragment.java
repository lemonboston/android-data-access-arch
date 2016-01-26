package com.gk.daas.screen;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gk.daas.R;
import com.gk.daas.bus.Bus;
import com.gk.daas.data.access.DataAccessInitiator;
import com.gk.daas.data.event.GetForecastProgressEvent;
import com.gk.daas.data.event.GetForecastSuccessEvent;
import com.gk.daas.di.ActivityComponent;
import com.gk.daas.dialog.ProgressDialog;
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
public class ParallelAndChainedCaseFragment extends Fragment {

    @Inject
    DataAccessInitiator dataAccessInitiator;

    @Inject
    Bus bus;

    @Bind(R.id.ResultText)
    TextView resultTextView;

    @Bind(R.id.Button_Execute)
    Button executeButton;

    @Bind(R.id.Text_WeatherUseCase)
    TextView weatherUseCase;

    @Bind(R.id.TechnicalUseCase_Description)
    TextView technicalUseCase;

    @Bind(R.id.ImplementationDescription)
    TextView implementationDescription;

    @Bind(R.id.EditText_City1)
    EditText city1Text;

    @Bind(R.id.EditText_City2)
    EditText city2Text;

    @Inject
    TemperatureFormatter temperatureFormatter;

    @Inject
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_use_case, container, false);
        ButterKnife.bind(this, view);

        executeButton.setText(R.string.GetForecast_Button);
        weatherUseCase.setText(R.string.WeatherUseCase_GetForecast);
        technicalUseCase.setText(R.string.UseCase_ParallelChained_Description);
        implementationDescription.setText(R.string.UseCase_ParallelChained_Implementation);
        city2Text.setVisibility(View.VISIBLE);

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
        dataAccessInitiator.getForecastForWarmerCity(city1Text.getText().toString(), city2Text.getText().toString());
    }

    @OnClick(R.id.Button_Clear)
    void onClearButtonClick() {
        resultTextView.setText(null);
    }

    // TODO maybe sticky is not needed any more that dialog fragment is fixed
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void onGetForecastProgressUpdate(GetForecastProgressEvent progress) {
        switch (progress) {
            case FIRST_STAGE_STARTED:
                progressDialog.showMessage(getString(R.string.ForecastProgress_Step1));
                break;
            case SECOND_STAGE_STARTED:
                progressDialog.showMessage(getString(R.string.ForecastProgress_Step2));
                break;
            case COMPLETED:
                progressDialog.dismiss();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGetForecastSuccess(GetForecastSuccessEvent event) {
        String temperature = temperatureFormatter.formatTempInKelvin(event.lastTemp);
        resultTextView.setText(event.cityName + ": " + temperature);
    }


}
