package com.gk.daas.screen;

import com.gk.daas.data.network.DataAccessError;

/**
 * @author Gabor_Keszthelyi
 */
public interface ErrorTranslator {

    String translate(DataAccessError error);
}