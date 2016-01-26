package com.gk.daas.screen.home;


import com.gk.daas.R;
import com.gk.daas.data.network.DataAccessError;
import com.gk.daas.framework.access.StringResAccess;
import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

/**
 * @author Gabor_Keszthelyi
 */
public class ErrorTranslatorImpl implements ErrorTranslator {

    private final StringResAccess stringResAccess;
    private final Log log;

    public ErrorTranslatorImpl(StringResAccess stringResAccess, LogFactory logFactory) {
        this.stringResAccess = stringResAccess;
        this.log = logFactory.create(getClass());
    }

    @Override
    public String translate(DataAccessError error) {
        switch (error) {
            case NO_INTERNET_CONNECTION:
                return stringResAccess.getString(R.string.ErrorMessage_noInternet);
            case UNKNOWN_HOST:
                return stringResAccess.getString(R.string.ErrorMessage_unknownHost);
            case NO_OFFLINE_DATA:
                return stringResAccess.getString(R.string.ErrorMessage_noOfflineData);
            case UNKNOWN:
                return stringResAccess.getString(R.string.ErrorMessage_unknownError);
            default:
                log.e("BUG!! Undefined error message for type: " + error);
                return stringResAccess.getString(R.string.ErrorMessage_unknownError);
        }
    }
}
