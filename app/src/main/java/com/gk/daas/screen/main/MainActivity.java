package com.gk.daas.screen.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gk.daas.R;
import com.gk.daas.bus.Bus;
import com.gk.daas.core.BaseActivity;
import com.gk.daas.data.access.DataAccessInitiator;
import com.gk.daas.data.event.GetTempSuccessEvent;
import com.gk.daas.data.network.DataAccessError;
import com.gk.daas.data.network.UseCase;
import com.gk.daas.di.ActivityComponent;
import com.gk.daas.di.DebugOptions;
import com.gk.daas.dialog.ErrorDialog;
import com.gk.daas.screen.home.ErrorTranslator;
import com.gk.daas.screen.second.SecondActivity;
import com.gk.daas.util.TemperatureFormatter;

import javax.inject.Inject;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * @author Gabor_Keszthelyi
 */
public class MainActivity extends BaseActivity {

    @Inject
    DataAccessInitiator dataAccessInitiator;

    @Inject
    Bus bus;

    @Inject
    TemperatureFormatter temperatureFormatter;

    @Inject
    ErrorTranslator errorTranslator;

    @Inject
    MainView view;

    @Inject
    ErrorDialog errorDialog;

    private DataAccessEventHandler eventHandler = new DataAccessEventHandler();

    private UseCase currentUseCase = UseCase.EMPTY_PLACEHOLDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComponent.Injector.inject(this);

        view.init();
        view.setUseCaseTitles(UseCase.getTitles(this));
        view.setUserActionListener(new UserActionHandler());

        bus.register(eventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(eventHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Menu_RealService:
                DebugOptions.MOCK_WEATHER_SERVICE = false;
                restartActivity();
                return true;
            case R.id.Menu_MockService:
                DebugOptions.MOCK_WEATHER_SERVICE = true;
                restartActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void restartActivity() {
        finish();
        startActivity(getIntent());
    }

    public class DataAccessEventHandler {

        @Subscribe(threadMode = ThreadMode.MainThread)
        public void onGetTempSuccess(GetTempSuccessEvent event) {
            view.hideProgressBar();
            String temperature = temperatureFormatter.formatTempInKelvin(event.temp);
            view.setResultText(temperature);
        }

        @Subscribe(threadMode = ThreadMode.MainThread)
        public void onDataAccessFailure(DataAccessError error) {
            view.hideProgressBar();
            String errorMessage = errorTranslator.translate(error);
            errorDialog.show(errorMessage);
        }

    }

    private class UserActionHandler implements MainView.UserActionListener {

        @Override
        public void onUseCaseSelected(int position) {
            MainActivity.this.currentUseCase = UseCase.get(position);

            view.hideEverything();

            if (currentUseCase == UseCase.EMPTY_PLACEHOLDER) {
                view.showGeneralDescription();
            } else {
                view.setExecuteButtonText(R.string.GetTemp_Button);
                view.setWeatherUseCaseDesc(R.string.WeatherUseCase_GetTemp);
                switch (currentUseCase) {
                    case BASIC:
                        view.showOtherScreenButton();
                        view.setTechnicalUseCaseDesc(R.string.UseCase_Basic_Description);
                        break;
                    case ERROR_HANDLING:
                        view.setTechnicalUseCaseDesc(R.string.UseCase_ErrorHandling_Desc);
                        view.setImplementationDesc(R.string.UseCase_ErrorHandling_Implementation);
                        view.setHowToTestDesc(R.string.UseCase_ErrorHandling_HowToTest);
                        break;
                    case ONGOING_CALL_HANDLING:
                        break;
                    case OFFLINE_STORAGE:
                        break;
                    case COMBINED:
                        break;
                    case PARALLEL_AND_CHAINED:
                        break;
                }
            }
        }

        @Override
        public void onExecuteButtonClick() {
            view.showProgressBar();
            String city = view.getCity1();
            if (currentUseCase == UseCase.PARALLEL_AND_CHAINED) {
                String city2 = view.getCity2();
                dataAccessInitiator.getForecastForWarmerCity(city, city2);
            } else {
                dataAccessInitiator.getWeather(currentUseCase, city);
            }
        }

        @Override
        public void onOtherScreenButtonClick() {
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }
    }
}
