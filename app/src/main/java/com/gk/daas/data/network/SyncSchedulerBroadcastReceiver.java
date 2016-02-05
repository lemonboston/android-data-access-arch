package com.gk.daas.data.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gk.daas.data.access.DataAccessInitiator;
import com.gk.daas.di.Injector;
import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

import javax.inject.Inject;

/**
 * @author Gabor_Keszthelyi
 */
public class SyncSchedulerBroadcastReceiver extends BroadcastReceiver {

    @Inject
    SyncScheduler syncScheduler;

    @Inject
    DataAccessInitiator dataAccessInitiator;

    @Inject
    LogFactory logFactory;

    Log log;

    @Override
    public void onReceive(Context context, Intent intent) {
        Injector.satisfy(this);
        this.log = logFactory.create(getClass());

        log.d("Sync broadcast received");
        syncScheduler.scheduleNext(intent);
        String city = intent.getStringExtra(SyncSchedulerImpl.KEY_CITY);
        dataAccessInitiator.getWeather(UseCase.OFFLINE_STORAGE, city);
    }

}
