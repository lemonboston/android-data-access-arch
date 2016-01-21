package com.gk.daas.log;

/**
 * Composite (as in Composite Pattern) {@link Log}.
 *
 * @author Gabor_Keszthelyi
 */
public interface CompositeLog extends Log {

    /**
     * Adds a {@link Log} to the composite.
     *
     * @param log log
     */
    void addLog(Log log);
}
