package com.gk.daas.data.network.connection;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.gk.daas.data.network.DataAccessError;
import com.gk.daas.data.network.ErrorHolderException;

import rx.Single;
import rx.SingleSubscriber;

/**
 * @author Gabor_Keszthelyi
 */
public class NetworkConnectionChecker {

    private final ConnectivityManager connectivityManager;

    public NetworkConnectionChecker(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public Single<Void> checkNetwork() {
        return Single.create(new Single.OnSubscribe<Void>() {
            @Override
            public void call(SingleSubscriber<? super Void> singleSubscriber) {
                if (isNetworkAvailable()) {
                    singleSubscriber.onSuccess(null);
                } else {
                    singleSubscriber.onError(new ErrorHolderException(DataAccessError.NO_INTERNET_CONNECTION));
                }
            }
        });
    }
}
