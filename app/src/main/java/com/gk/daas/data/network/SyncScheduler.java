package com.gk.daas.data.network;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

/**
 * @author Gabor_Keszthelyi
 */
public class SyncScheduler {

    public static final long DEFAULT_INTERVAL_IN_MILLIS = 10_000;

    private static final String KEY_INTERVAL = "key_interval";
    private static final double BACKOFF_MULTIPLIER = 1.1;

    private final AlarmManager alarmManager;
    private final Context context;
    private final Log log;

    public SyncScheduler(Context context, LogFactory logFactory) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.log = logFactory.create(getClass());
    }

    public void scheduleSyncing(long interval) {
        log.d("Schedule sync in: " + interval / 1000 + " seconds");
        long start = System.currentTimeMillis() + interval;
        PendingIntent pendingIntent = createPendingIntent(interval);
        alarmManager.set(AlarmManager.RTC, start, pendingIntent);
    }

    public void scheduleNext(Intent intent) {
        long interval = intent.getLongExtra(KEY_INTERVAL, DEFAULT_INTERVAL_IN_MILLIS);
        scheduleSyncing((long) (interval * BACKOFF_MULTIPLIER));
    }

    public void stopSyncing() {
        log.d("Cancel sync");
        alarmManager.cancel(createPendingIntent(0));
    }

    private PendingIntent createPendingIntent(long interval) {
        Intent intent = new Intent(context, SyncSchedulerBroadcastReceiver.class);
        intent.putExtra(KEY_INTERVAL, interval);
        return PendingIntent.getBroadcast(context, 456, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
