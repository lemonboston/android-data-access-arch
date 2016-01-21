package com.gk.daas.data.network;

import com.gk.daas.core.OurException;

/**
 * @author Gabor_Keszthelyi
 */
public class ErrorHolderException extends OurException {

    public final DataAccessError dataAccessError;

    public ErrorHolderException(DataAccessError dataAccessError) {
        this.dataAccessError = dataAccessError;
    }
}
