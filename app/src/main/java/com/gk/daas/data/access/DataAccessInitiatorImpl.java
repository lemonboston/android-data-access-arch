package com.gk.daas.data.access;

import android.content.Context;
import android.content.Intent;

import com.gk.daas.data.network.NetworkServiceIntentHelper;

/**
 * @author Gabor_Keszthelyi
 */
// TODO update intent helper after re-organize complete
public class DataAccessInitiatorImpl implements DataAccessInitiator {

    private final Context context;
    private final NetworkServiceIntentHelper intentHelper;

    public DataAccessInitiatorImpl(Context context, NetworkServiceIntentHelper intentHelper) {
        this.context = context;
        this.intentHelper = intentHelper;
    }

    @Override
    public void getTemperature_allInOne(String city) {
        Intent intent = intentHelper.createGetTempIntent(city);
        context.startService(intent);
    }

    @Override
    public void getTemperature_wOfflineLocalStore(String city) {
        Intent intent = intentHelper.createGetTempOfflineStoreIntent(city);
        context.startService(intent);
    }

    @Override
    public void getForecastForWarmerCity(String city1, String city2) {
        Intent intent = intentHelper.createGetForecastForWarmerCity(city1, city2);
        context.startService(intent);
    }

    @Override
    public void getTemperature_basic(String city) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getTemperature_wErrorHandling(String city) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getTemperature_wOngoingHandling(String city) {
        throw new UnsupportedOperationException();
    }
}
