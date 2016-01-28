package com.gk.daas.data.network;

import com.gk.daas.data.network.connection.NoInternetException;
import com.gk.daas.data.store.NoOfflineDataException;
import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * @author Gabor_Keszthelyi
 */
public class ErrorInterpreterImpl implements ErrorInterpreter {

    private final Log log;

    public ErrorInterpreterImpl(LogFactory logFactory) {
        this.log = logFactory.create(getClass());
    }

    @Override
    public DataAccessError interpret(Throwable throwable) {
        if (throwable instanceof NoInternetException) {
            return DataAccessError.NO_INTERNET_CONNECTION;
        } else if (throwable instanceof HttpException && (((HttpException) throwable)).code() == 401) {
            return DataAccessError.UNAUTHORIZED;
        } else if (throwable instanceof UnknownHostException) {
            return DataAccessError.UNKNOWN_HOST;
        } else if (throwable instanceof NoOfflineDataException) {
            return DataAccessError.NO_OFFLINE_DATA;
        } else {
            log.e("Unknown throwable: " + throwable);
            return DataAccessError.UNKNOWN;
        }
    }
}
