package com.gk.daas.data.network;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gk.daas.core.Config;
import com.gk.daas.framework.access.Toaster;
import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

/**
 * @author Gabor_Keszthelyi
 */
public class SyncSchedulerImpl implements SyncScheduler {

    public static final String KEY_CITY = "key_city";

    private static final String KEY_INTERVAL = "key_interval";
    private static final int REQUEST_CODE = 456;

    private final AlarmManager alarmManager;
    private final Context context;
    private final Toaster toaster;
    private final Log log;

    public SyncSchedulerImpl(Context context, LogFactory logFactory, Toaster toaster) {
        this.context = context;
        this.toaster = toaster;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.log = logFactory.create(getClass());
    }

    @Override
    public void startSyncing(String city) {
        scheduleSyncing(city, Config.Syncing.DEFAULT_INTERVAL_IN_MILLIS);
    }

    @Override
    public void scheduleNext(Intent intent) {
        long previousInterval = intent.getLongExtra(KEY_INTERVAL, Config.Syncing.DEFAULT_INTERVAL_IN_MILLIS);
        long newInterval = (long) (previousInterval * Config.Syncing.BACKOFF_EXPONENTIAL_BASE);
        String city = intent.getStringExtra(KEY_CITY);
        scheduleSyncing(city, newInterval);
    }

    @Override
    public void stopSyncing() {
        log.d("Cancel sync");
        toaster.showToast("Stopping sync");
        alarmManager.cancel(createPendingIntent(0, null));
    }

    private void scheduleSyncing(String city, long interval) {
        String msg = String.format("Scheduled sync for city %s in %.1f seconds.", city, (float) interval / 1000);
        log.d(msg);
        toaster.showToast(msg);
        long start = System.currentTimeMillis() + interval;
        PendingIntent pendingIntent = createPendingIntent(interval, city);
        alarmManager.set(AlarmManager.RTC, start, pendingIntent);
    }

    private PendingIntent createPendingIntent(long interval, String city) {
        Intent intent = new Intent(context, SyncSchedulerBroadcastReceiver.class);
        intent.putExtra(KEY_INTERVAL, interval);
        intent.putExtra(KEY_CITY, city);
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
