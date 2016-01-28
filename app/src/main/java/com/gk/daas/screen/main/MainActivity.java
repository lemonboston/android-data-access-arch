package com.gk.daas.screen.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gk.daas.R;
import com.gk.daas.bus.Bus;
import com.gk.daas.core.BaseActivity;
import com.gk.daas.data.access.DataAccessInitiator;
import com.gk.daas.data.event.GetTempStoreSuccessEvent;
import com.gk.daas.data.network.DataAccessError;
import com.gk.daas.data.network.UseCase;
import com.gk.daas.di.ActivityComponent;
import com.gk.daas.di.DebugOptions;
import com.gk.daas.framework.access.Toaster;
import com.gk.daas.screen.home.ErrorTranslator;
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
    Toaster toaster;

    @Inject
    MainView view;

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
        public void onGetTempSuccess(GetTempStoreSuccessEvent event) {
            view.hideProgressBar();
            String temperature = temperatureFormatter.formatTempInKelvin(event.temp);
            view.setResultText(temperature);
        }

        @Subscribe(threadMode = ThreadMode.MainThread)
        public void onDataAccessFailure(DataAccessError error) {
            view.hideProgressBar();
            String errorMessage = errorTranslator.translate(error);
            toaster.showToast(errorMessage);
        }

    }

    private class UserActionHandler implements MainView.UserActionListener {

        @Override
        public void onUseCaseSelected(int position) {
            MainActivity.this.currentUseCase = UseCase.get(position);

            if (currentUseCase == UseCase.EMPTY_PLACEHOLDER) {
                view.hideEverything();
                view.showGeneralDescription();
            } else {
                view.showEverything();
                switch (currentUseCase) {
                    case EMPTY_PLACEHOLDER:
                        view.hideEverything();
                        break;
                    case BASIC:
                        view.showEverything();
                        break;
                    case ERROR_HANDLING:
                        view.showEverything();
                        break;
                    case ONGOING_CALL_HANDLING:
                        view.showEverything();
                        break;
                    case OFFLINE_STORAGE:
                        view.showEverything();
                        break;
                    case COMBINED:
                        view.showEverything();
                        break;
                    case PARALLEL_AND_CHAINED:
                        view.showEverything();
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
    }
}
