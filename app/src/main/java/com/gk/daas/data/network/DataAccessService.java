package com.gk.daas.data.network;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;

import com.gk.daas.data.access.DataAccessController;
import com.gk.daas.di.AppComponent;
import com.gk.daas.di.ServiceComponent;
import com.gk.daas.framework.access.Toaster;
import com.gk.daas.log.Log;

import javax.inject.Inject;

/**
 * @author Gabor_Keszthelyi
 */
public class DataAccessService extends Service implements TaskCounter.AllTasksFinishedListener {

    @Inject
    DataAccessController dataAccessController;

    @Inject
    NetworkServiceIntentHelper intentHelper;

    @Inject
    Toaster toaster;

    @Inject
    TaskCounter taskCounter;

    Log log;

    private HandlerThread handlerThread;

    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceComponent.Injector.inject(this);
        log = AppComponent.Holder.getInstance().getLogFactory().create(getClass());

        handlerThread = new HandlerThread(getClass().getSimpleName(), Process.THREAD_PRIORITY_MORE_FAVORABLE);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        taskCounter.setAllTaskFinishedListener(this);
        taskCounter.setHandler(handler);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log.d("Intent received: " + intent + " flags: " + flags + " startId: " + startId);
        if (intent != null) {
            taskCounter.taskStarted();
            handler.post(() -> handleIntent(intent));
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        log.d("Task removed");
        toaster.showToast("Task removed");
    }

    private void handleIntent(Intent intent) {
        NetworkUseCase useCase = intentHelper.extractNetworkUseCase(intent);
        switch (useCase) {
            case GET_TEMP:
                processGetTemp(intent);
                break;
            case GET_FORECAST_FOR_WARMEST_CITY:
                processGetForecastForWarmer(intent);
                break;
            case GET_TEMPERATURE_DIFF:
                processGetTempDiff(intent);
                break;
            case GET_TEMP_OFFLINE_STORE:
                processGetTempOfflineStore(intent);
                break;
        }
    }

    private void processGetTemp(Intent intent) {
        String city = intentHelper.extractCity(intent);
        dataAccessController.getTemperature(city);
    }

    private void processGetForecastForWarmer(Intent intent) {
        String city1 = intentHelper.extractCity1(intent);
        String city2 = intentHelper.extractCity2(intent);
        dataAccessController.getForecastForWarmestCity(city1, city2);
    }

    private void processGetTempDiff(Intent intent) {
        String city1 = intentHelper.extractCity1(intent);
        String city2 = intentHelper.extractCity2(intent);
        dataAccessController.getTemperatureDiff(city1, city2);
    }

    private void processGetTempOfflineStore(Intent intent) {
        String city = intentHelper.extractCity(intent);
        dataAccessController.getTemperature_OfflineLocalStore(city);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log.d("Destroying");
        handlerThread.quitSafely();
    }

    @Override
    public void onAllTasksFinished() {
        stopSelf();
        log.d("No tasks left, stopping self.");
    }
}
