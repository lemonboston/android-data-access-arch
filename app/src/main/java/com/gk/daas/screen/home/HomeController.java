package com.gk.daas.screen.home;

import com.gk.daas.bus.Bus;
import com.gk.daas.data.access.DataAccessInitiator;
import com.gk.daas.data.event.GetDiffSuccessEvent;
import com.gk.daas.data.event.GetForecastFirstPartSuccessEvent;
import com.gk.daas.data.event.GetForecastForWarmestCitySuccessEvent;
import com.gk.daas.data.event.GetTempStoreSuccessEvent;
import com.gk.daas.data.event.GetTempSuccessEvent;
import com.gk.daas.data.network.DataAccessError;
import com.gk.daas.data.network.SyncScheduler;
import com.gk.daas.di.DevTweaks;
import com.gk.daas.dialog.ProgressDialog;
import com.gk.daas.framework.access.Navigator;
import com.gk.daas.framework.access.Toaster;
import com.gk.daas.framework.listen.OnCreate;
import com.gk.daas.framework.listen.OnDestroy;
import com.gk.daas.screen.second.SecondActivity;
import com.gk.daas.util.TemperatureFormatter;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * @author Gabor_Keszthelyi
 */
public class HomeController implements OnCreate, OnDestroy {

    private final HomeUi ui;
    private final DataAccessInitiator dataAccessInitiator;
    private final SyncScheduler syncScheduler;
    private final Bus bus;
    private final ProgressDialog progressDialog;
    private final Navigator navigator;
    private final TemperatureFormatter temperatureFormatter;
    private final Toaster toaster;
    private final ErrorTranslator errorTranslator;

    private EventHandler eventHandler;

    public HomeController(HomeUi ui, DataAccessInitiator dataAccessInitiator, SyncScheduler syncScheduler, Bus bus, ProgressDialog progressDialog, Navigator navigator, TemperatureFormatter temperatureFormatter, Toaster toaster, ErrorTranslator errorTranslator) {
        this.ui = ui;
        this.dataAccessInitiator = dataAccessInitiator;
        this.syncScheduler = syncScheduler;
        this.bus = bus;
        this.progressDialog = progressDialog;
        this.navigator = navigator;
        this.temperatureFormatter = temperatureFormatter;
        this.toaster = toaster;
        this.errorTranslator = errorTranslator;
    }

    @Override
    public void onCreate() {
        ui.init();
//        ui.setUserActionListener(new UserActionHandler());
//        eventHandler = new EventHandler();
//        bus.register(eventHandler);
    }

    @Override
    public void onDestroy() {
//        bus.unregister(eventHandler);
    }

    private class UserActionHandler implements HomeUi.UserActionListener {

        @Override
        public void onGetTempButton() {
            dataAccessInitiator.getTemperature("Budapest");
        }

        @Override
        public void onGetWarmestCityForecast() {
            progressDialog.show("Step 1\nGetting warmest city..");
            dataAccessInitiator.getForecastForWarmestCity("Budapest", "Vienna");
        }

        @Override
        public void onClearButtonClick() {
            ui.clearResultTexts();
        }

        @Override
        public void onGoToOtherScreen() {
            navigator.startActivity(SecondActivity.class);
            bus.unregister(this);
        }

        @Override
        public void onGetDiffButton() {
            dataAccessInitiator.getTemperatureDiff("Budapest", "Vienna");
        }

        @Override
        public void onStartBackgroundSync() {
            syncScheduler.scheduleSyncing(SyncScheduler.DEFAULT_INTERVAL_IN_MILLIS);
        }

        @Override
        public void onStopBackgroundSync() {
            syncScheduler.stopSyncing();
        }

        @Override
        public void onServiceRadioButtonClick(boolean isMockService) {
            DevTweaks.MOCK_WEATHER_SERVICE = isMockService;
            navigator.reloadThisActivity();
        }

        @Override
        public void onGetTempStoreButton() {
            dataAccessInitiator.getTemperature_OfflineLocalStore("Budapest");
        }
    }

    public class EventHandler {

        @Subscribe(threadMode = ThreadMode.MainThread)
        public void onGetTempSuccess(GetTempSuccessEvent event) {
            ui.setTempResultText(temperatureFormatter.formatTempInKelvin(event.temp));
        }

        @Subscribe(threadMode = ThreadMode.MainThread)
        public void onGetTempOfflineStoreSuccess(GetTempStoreSuccessEvent event) {
            ui.setTempOfflineStoreResultText(temperatureFormatter.formatTempInKelvin(event.temp));
        }

        @Subscribe(threadMode = ThreadMode.MainThread)
        public void onGetDiffSuccess(GetDiffSuccessEvent event) {
            ui.setDiffResultText(temperatureFormatter.formatTempDiff(event.tempDiff));
        }

        @Subscribe(threadMode = ThreadMode.MainThread)
        public void onGetForecastFirstPartSuccess(GetForecastFirstPartSuccessEvent event) {
            progressDialog.updateMessage("Step 2\nGetting forecast..");
        }

        @Subscribe(threadMode = ThreadMode.MainThread)
        public void onGetWarmestCityForecastSuccess(GetForecastForWarmestCitySuccessEvent event) {
            progressDialog.dismiss();
            ui.setForecastResultText(temperatureFormatter.formatTempInKelvin(event.lastTemp));
        }

        @Subscribe(threadMode = ThreadMode.MainThread)
        public void onGetDataAccessError(DataAccessError error) {
            toaster.showToast(errorTranslator.translate(error));
        }


    }

}
