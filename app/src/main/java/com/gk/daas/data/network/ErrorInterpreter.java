package com.gk.daas.data.network;

/**
 * @author Gabor_Keszthelyi
 */
public interface ErrorInterpreter {

    DataAccessError interpret(Throwable throwable);
}
