package com.gk.daas.app_module.screen;


import com.gk.daas.R;
import com.gk.daas.app_module.data_access.error.DataAccessError;
import com.gk.daas.app_module.framework.access.StringResAccess;
import com.gk.daas.app_module.log.Log;
import com.gk.daas.app_module.log.LogFactory;

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
            case UNAUTHORIZED:
                return stringResAccess.getString(R.string.ErrorMessage_unauthorized);
            case UNKNOWN:
                return stringResAccess.getString(R.string.ErrorMessage_unknownError);
            default:
                log.e("BUG!! Undefined error message for type: " + error);
                return stringResAccess.getString(R.string.ErrorMessage_unknownError);
        }
    }
}
