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
import com.gk.daas.data.event.DoubleLoadFinishEvent;
import com.gk.daas.data.event.GetForecastProgressEvent;
import com.gk.daas.data.event.GetForecastSuccessEvent;
import com.gk.daas.data.event.GetTempSuccessEvent;
import com.gk.daas.data.event.RetryEvent;
import com.gk.daas.data.network.DataAccessError;
import com.gk.daas.data.network.SyncScheduler;
import com.gk.daas.data.network.UseCase;
import com.gk.daas.data.store.DataStore;
import com.gk.daas.di.Injector;
import com.gk.daas.dialog.ErrorDialog;
import com.gk.daas.dialog.ProgressDialog;
import com.gk.daas.dialog.ServiceSelectorDialog;
import com.gk.daas.screen.ErrorTranslator;
import com.gk.daas.screen.second.SecondActivity;
import com.gk.daas.util.TemperatureFormatter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;


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

    @Inject
    ProgressDialog progressDialog;

    @Inject
    DataStore dataStore;

    @Inject
    ServiceSelectorDialog serviceSelectorDialog;

    @Inject
    SyncScheduler syncScheduler;

    private DataAccessEventHandler eventHandler = new DataAccessEventHandler();
    private UseCase currentUseCase = UseCase.EMPTY_PLACEHOLDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.satisfy(this);

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
            case R.id.Menu_ServiceSelector:
                serviceSelectorDialog.show();
                return true;
            case R.id.Menu_ClearDataStore:
                dataStore.clearStore();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class DataAccessEventHandler {

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onGetTempSuccess(GetTempSuccessEvent event) {
            view.hideProgressBar();
            progressDialog.dismiss();
            view.setResultText(event.temperature.toString());
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onDataAccessFailure(DataAccessError error) {
            view.hideProgressBar();
            progressDialog.dismiss();
            String errorMessage = errorTranslator.translate(error);
            errorDialog.show(errorMessage);
        }

        @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
        public void onProgressUpdate(GetForecastProgressEvent progress) {
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

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onForecastSuccess(GetForecastSuccessEvent event) {
            view.setResultText(event.forecast.toString());
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onRetryEvent(RetryEvent retry) {
            view.hideProgressBar();
            progressDialog.showMessage(String.format("Retry #%d", retry.retryCount));
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onDoubleLoadFinish(DoubleLoadFinishEvent event) {
            view.hideRefreshIndicator();
        }
    }

    private class UserActionHandler implements MainView.UserActionListener {

        @Override
        public void onUseCaseSelected(int position) {
            MainActivity.this.currentUseCase = UseCase.get(position);

            view.hideEverything();

            switch (currentUseCase) {
                case EMPTY_PLACEHOLDER:
                    view.showGeneralDescription();
                    break;
                case BASIC:
                    view.showOtherScreenButton();
                    view.setExecuteButtonText(currentUseCase.button);
                    view.setWeatherUseCaseDesc(currentUseCase.weatherUseCase);
                    view.setTechnicalUseCaseDesc(currentUseCase.technicalDesc);
                    view.setImplementationDesc(currentUseCase.implementationDesc);
                    view.setHowToTestDesc(currentUseCase.testDesc);
                    break;
                case PARALLEL_AND_CHAINED:
                    view.showCity2();
                    view.setExecuteButtonText(currentUseCase.button);
                    view.setWeatherUseCaseDesc(currentUseCase.weatherUseCase);
                    view.setTechnicalUseCaseDesc(currentUseCase.technicalDesc);
                    view.setImplementationDesc(currentUseCase.implementationDesc);
                    view.setHowToTestDesc(currentUseCase.testDesc);
                    break;
                default:
                    view.setExecuteButtonText(currentUseCase.button);
                    view.setWeatherUseCaseDesc(currentUseCase.weatherUseCase);
                    view.setTechnicalUseCaseDesc(currentUseCase.technicalDesc);
                    view.setImplementationDesc(currentUseCase.implementationDesc);
                    view.setHowToTestDesc(currentUseCase.testDesc);
            }
        }

        @Override
        public void onExecuteButtonClick() {
            String city = view.getCity1();
            switch (currentUseCase) {
                case BASIC:
                case ERROR_HANDLING:
                case ONGOING_CALL_HANDLING:
                case OFFLINE_STORAGE:
                case RETRY:
                case COMBINED:
                    view.showProgressBar();
                    dataAccessInitiator.getWeather(currentUseCase, city);
                    break;
                case DOUBLE_LOAD:
                    view.showRefreshingIndicator();
                    dataAccessInitiator.getWeather(currentUseCase, city);
                    break;
                case CANCELLABLE:
                    dataAccessInitiator.getWeather(currentUseCase, city);
                    runOnUiThread(dataAccessInitiator::cancelCall); // almost immediately requesting cancel as well to test (see logs)
                    break;
                case PARALLEL_AND_CHAINED:
                    String city2 = view.getCity2();
                    dataAccessInitiator.getForecastForWarmerCity(city, city2);
                    break;
                case SYNC:
                    syncScheduler.startSyncing(city);
                    break;
            }
        }

        @Override
        public void onOtherScreenButtonClick() {
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }

        @Override
        public void onXbuttonClick() {
            view.clearResultText();
            if (currentUseCase == UseCase.SYNC) {
                syncScheduler.stopSyncing();
            }
        }
    }
}