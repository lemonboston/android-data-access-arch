package com.gk.daas.data.network.connection;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import rx.Single;

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
        return Single.create(singleSubscriber -> {
            if (isNetworkAvailable()) {
                singleSubscriber.onSuccess(null);
            } else {
                singleSubscriber.onError(new NoInternetException());
            }
        });
    }
}
