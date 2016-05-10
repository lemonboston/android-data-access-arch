package com.gk.daas.app_module.log;

/**
 * @author Gabor_Keszthelyi
 */
public class LogFactoryImpl implements LogFactory {

    @Override
    public Log create(String tag) {
        return new LogCatLog(tag);
    }

    @Override
    public Log create(Class<?> classForTag) {
        return new LogCatLog(classForTag.getSimpleName());
    }
}
