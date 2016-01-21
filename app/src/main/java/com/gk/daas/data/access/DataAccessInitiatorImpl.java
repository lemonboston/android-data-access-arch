package com.gk.daas.data.access;

import android.content.Context;
import android.content.Intent;

import com.gk.daas.data.network.NetworkServiceIntentHelper;

/**
 * @author Gabor_Keszthelyi
 */
// TODO Same interface for this and the one that carries out the stuff in the service? maybe just extend that one marking it initiator, to differentiate, control access
public class DataAccessInitiatorImpl implements DataAccessInitiator {

    private final Context context;
    private final NetworkServiceIntentHelper intentHelper;

    public DataAccessInitiatorImpl(Context context, NetworkServiceIntentHelper intentHelper) {
        this.context = context;
        this.intentHelper = intentHelper;
    }

    @Override
    public void getTemperature(String city) {
        Intent intent = intentHelper.createGetTempIntent(city);
        context.startService(intent);
    }

    @Override
    public void getTemperature_OfflineLocalStore(String city) {
        Intent intent = intentHelper.createGetTempOfflineStoreIntent(city);
        context.startService(intent);
    }

    @Override
    public void getForecastForWarmestCity(String city1, String city2) {
        Intent intent = intentHelper.createGetForecastForWarmestCity(city1, city2);
        context.startService(intent);
    }

    @Override
    public void getTemperatureDiff(String city1, String city2) {
        Intent intent = intentHelper.createGetTemperatureDiff(city1, city2);
        context.startService(intent);
    }
}
