package com.gk.daas.data.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gk.daas.data.access.DataAccessInitiator;
import com.gk.daas.di.AppComponent;
import com.gk.daas.log.Log;

import javax.inject.Inject;

/**
 * @author Gabor_Keszthelyi
 */
public class SyncSchedulerBroadcastReceiver extends BroadcastReceiver {

    @Inject
    SyncScheduler syncScheduler;

    @Inject
    DataAccessInitiator dataAccessInitiator;

    Log log;

    @Override
    public void onReceive(Context context, Intent intent) {
        inject();

        log.d("Sync broadcast received");
        syncScheduler.scheduleNext(intent);
        // TODO which method to use here
        dataAccessInitiator.getTemperature_allInOne("Miami");
    }

    private void inject() {
        AppComponent.Holder.getInstance().inject(this);
        this.log = AppComponent.Holder.getInstance().getLogFactory().create(getClass());
    }
}
