package com.gk.daas.log;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of {@link CompositeLog} with some optimization on the iterations.
 *
 * @author Gabor_Keszthelyi
 */
public class CompositeLogImpl implements CompositeLog {

    private static final int INITIAL_CAPACITY = 3;

    private List<Log> loggers = new ArrayList<>(INITIAL_CAPACITY);

    private int cachedSize = 0;

    @Override
    public void addLog(Log log) {
        loggers.add(log);
        cachedSize = loggers.size();
    }

    @Override
    public void v(String msg) {
        for (int i = 0; i < cachedSize; i++) {
            loggers.get(i).v(msg);
        }
    }

    @Override
    public void d(String msg) {
        for (int i = 0; i < cachedSize; i++) {
            loggers.get(i).d(msg);
        }
    }

    @Override
    public void i(String msg) {
        for (int i = 0; i < cachedSize; i++) {
            loggers.get(i).i(msg);
        }
    }

    @Override
    public void w(String msg) {
        for (int i = 0; i < cachedSize; i++) {
            loggers.get(i).w(msg);
        }
    }

    @Override
    public void e(String msg) {
        for (int i = 0; i < cachedSize; i++) {
            loggers.get(i).e(msg);
        }
    }

    @Override
    public void e(String msg, Throwable cause) {
        for (int i = 0; i < cachedSize; i++) {
            loggers.get(i).e(msg, cause);
        }
    }
}
