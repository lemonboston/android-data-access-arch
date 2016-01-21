package com.gk.daas.screen.home;

import android.widget.RadioButton;
import android.widget.TextView;

import com.gk.daas.R;
import com.gk.daas.di.DevTweaks;
import com.gk.daas.framework.access.ViewToolkit;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Gabor_Keszthelyi
 */
// TODO create a spinner with use-cases..
public class HomeUiImpl implements HomeUi {

    @Bind(R.id.GetTemp_ResultText)
    TextView tempResultText;

    @Bind(R.id.GetDiff_ResultText)
    TextView diffResultText;

    @Bind(R.id.GetWarmestForecast_ResultText)
    TextView forecastResultText;

    @Bind(R.id.GetTemp_Store_ResultText)
    TextView tempStoreResultText;

    @Bind(R.id.Radio_RealService)
    RadioButton realServiceButton;

    @Bind(R.id.Radio_MockService)
    RadioButton mockServiceButton;

    private final ViewToolkit viewToolkit;

    private UserActionListener listener;

    public HomeUiImpl(ViewToolkit viewToolkit) {
        this.viewToolkit = viewToolkit;
    }

    @Override
    public void init() {
        viewToolkit.setContentView(R.layout.activity_home);
        viewToolkit.injectViews(this);

        initServiceButton();
    }

    @Override
    public void setTempResultText(String text) {
        tempResultText.setText(text);
    }

    @Override
    public void setTempOfflineStoreResultText(String text) {
        tempStoreResultText.setText(text);
    }

    @Override
    public void setDiffResultText(String text) {
        diffResultText.setText(text);
    }

    @Override
    public void setForecastResultText(String text) {
        forecastResultText.setText(text);
    }

    @Override
    public void clearResultTexts() {
        tempResultText.setText(null);
        diffResultText.setText(null);
        forecastResultText.setText(null);
        tempStoreResultText.setText(null);
    }

    @Override
    public void setUserActionListener(UserActionListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.Button_GetTemp)
    void onGetTempButtonClick() {
        listener.onGetTempButton();
    }

    @OnClick(R.id.Button_GetTempDiff)
    void onGetDiffButtonClick() {
        listener.onGetDiffButton();
    }

    @OnClick(R.id.Button_GetWarmesForecast)
    void onGetForecastButtonClick() {
        listener.onGetWarmestCityForecast();
    }

    @OnClick(R.id.Button_Clear)
    void onClearButtonClick() {
        listener.onClearButtonClick();
    }

    @OnClick(R.id.Button_StartSync)
    void onStartSyncButtonClick() {
        listener.onStartBackgroundSync();
    }

    @OnClick(R.id.Button_StopSync)
    void onStopSyncButtonClick() {
        listener.onStopBackgroundSync();
    }

    @OnClick(R.id.Button_GoToOtherScreen)
    void onGoToOtherScreenButtonClick() {
        listener.onGoToOtherScreen();
    }

    @OnClick(R.id.Radio_RealService)
    void onRealServiceRadioButtonClick() {
        listener.onServiceRadioButtonClick(false);
    }

    @OnClick(R.id.Radio_MockService)
    void onMockServiceRadioButtonClick() {
        listener.onServiceRadioButtonClick(true);
    }

    @OnClick(R.id.Button_GetTemp_Store)
    void onGetTempStoreClick() {
        listener.onGetTempStoreButton();
    }

    private void initServiceButton() {
        if (DevTweaks.MOCK_WEATHER_SERVICE) {
            mockServiceButton.setChecked(true);
        } else {
            realServiceButton.setChecked(true);
        }
    }
}
