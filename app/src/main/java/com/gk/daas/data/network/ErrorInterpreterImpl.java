package com.gk.daas.data.network;

import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

import java.net.UnknownHostException;

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
        if (throwable instanceof ErrorHolderException) {
            return ((ErrorHolderException) throwable).dataAccessError;
        } else if (throwable instanceof UnknownHostException) {
            return DataAccessError.UNKNOWN_HOST;
        } else {
            log.e("Unknown throwable: " + throwable);
            return DataAccessError.UNKNOWN;
        }
    }
}
