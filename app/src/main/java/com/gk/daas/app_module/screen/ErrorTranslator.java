package com.gk.daas.app_module.screen;

import com.gk.daas.app_module.data_access.error.DataAccessError;

/**
 * @author Gabor_Keszthelyi
 */
public interface ErrorTranslator {

    String translate(DataAccessError error);
}
