package com.gk.daas.data.network;

import android.content.Intent;

import com.gk.daas.BuildConfig;


/**
 * @author Gabor_Keszthelyi
 */
public class NetworkServiceIntentHelper {

    public static final String KEY_CITY = "key_city";
    public static final String KEY_CITY_1 = "key_city_1";
    public static final String KEY_CITY_2 = "key_city_2";

    public NetworkUseCase extractNetworkUseCase(Intent intent) {
        return NetworkUseCase.valueOf(intent.getAction());
    }

    public Intent createGetTempIntent(String city) {
        Intent intent = new Intent();
        intent.setClassName(BuildConfig.APPLICATION_ID, DataAccessService.class.getName());
        intent.setAction(NetworkUseCase.GET_TEMP.name());
        intent.putExtra(KEY_CITY, city);
        return intent;
    }

    public Intent createGetForecastForWarmerCity(String city1, String city2) {
        Intent intent = new Intent();
        intent.setClassName(BuildConfig.APPLICATION_ID, DataAccessService.class.getName());
        intent.setAction(NetworkUseCase.GET_FORECAST_FOR_WARMEST_CITY.name());
        intent.putExtra(KEY_CITY_1, city1);
        intent.putExtra(KEY_CITY_2, city2);
        return intent;
    }

    public String extractCity(Intent intent) {
        return intent.getStringExtra(KEY_CITY);
    }

    public String extractCity1(Intent intent) {
        return intent.getStringExtra(KEY_CITY_1);
    }

    public String extractCity2(Intent intent) {
        return intent.getStringExtra(KEY_CITY_2);
    }

    public Intent createGetTempOfflineStoreIntent(String city) {
        Intent intent = new Intent();
        intent.setClassName(BuildConfig.APPLICATION_ID, DataAccessService.class.getName());
        intent.setAction(NetworkUseCase.GET_TEMP_OFFLINE_STORE.name());
        intent.putExtra(KEY_CITY, city);
        return intent;
    }
}
