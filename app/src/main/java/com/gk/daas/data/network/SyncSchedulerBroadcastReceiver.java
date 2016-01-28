package com.gk.daas.data.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gk.daas.data.access.DataAccessInitiator;
import com.gk.daas.di.AppComponent;
import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

import javax.inject.Inject;

/**
 * @author Gabor_Keszthelyi
 */
public class SyncSchedulerBroadcastReceiver extends BroadcastReceiver {

    Log log = LogFactory.createLog(SyncSchedulerBroadcastReceiver.class);

    @Inject
    SyncScheduler syncScheduler;

    @Inject
    DataAccessInitiator dataAccessInitiator;

    @Override
    public void onReceive(Context context, Intent intent) {
        AppComponent.Holder.getInstance().inject(this);

        log.d("Sync broadcast received");
        syncScheduler.scheduleNext(intent);
        dataAccessInitiator.getWeather(UseCase.BASIC, "London");
    }

}
