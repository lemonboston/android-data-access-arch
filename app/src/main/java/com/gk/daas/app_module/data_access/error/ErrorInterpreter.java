package com.gk.daas.app_module.data_access.error;

/**
 * @author Gabor_Keszthelyi
 */
public interface ErrorInterpreter {

    DataAccessError interpret(Throwable throwable);
}
