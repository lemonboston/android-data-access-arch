package com.gk.daas.data.network;

import android.content.Intent;

/**
 * @author Gabor_Keszthelyi
 */
public interface SyncScheduler {

    void startSyncing(String city);

    void scheduleNext(Intent intent);

    void stopSyncing();
}
