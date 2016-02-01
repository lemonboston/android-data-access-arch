package com.gk.daas.log;

/**
 * @author Gabor_Keszthelyi
 */
public interface LogFactory {

    Log create(String tag);

    Log create(Class<?> classForTag);

}
