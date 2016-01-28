package com.gk.daas.data.access;

import android.content.Context;
import android.content.Intent;

import com.gk.daas.data.network.NetworkServiceIntentHelper;
import com.gk.daas.data.network.UseCase;

/**
 * @author Gabor_Keszthelyi
 */
public class DataAccessInitiatorImpl implements DataAccessInitiator {

    private final Context context;
    private final NetworkServiceIntentHelper intentHelper;

    public DataAccessInitiatorImpl(Context context, NetworkServiceIntentHelper intentHelper) {
        this.context = context;
        this.intentHelper = intentHelper;
    }

    @Override
    public void getWeather(UseCase useCase, String city) {
        Intent intent = intentHelper.createGetWeatherIntent(useCase, city);
        context.startService(intent);
    }

    @Override
    public void getForecastForWarmerCity(String city1, String city2) {
        Intent intent = intentHelper.createGetForecastForWarmerCity(city1, city2);
        context.startService(intent);
    }

}
