package com.gk.daas.app_module.log;

/**
 * {@link Log} implementation that logs to logcat.
 *
 * @author Gabor_Keszthelyi
 */
public class LogCatLog implements Log {

    private String tag = APP_TAG_PREFIX;

    public LogCatLog(String tag) {
        this.tag = APP_TAG_PREFIX + tag;
    }

    @Override
    public void v(String msg) {
        android.util.Log.v(tag, msg);
    }

    @Override
    public void d(String msg) {
        android.util.Log.d(tag, msg);
    }

    @Override
    public void i(String msg) {
        android.util.Log.i(tag, msg);
    }

    @Override
    public void w(String msg) {
        android.util.Log.w(tag, msg);
    }

    @Override
    public void e(String msg) {
        android.util.Log.e(tag, msg);
    }

    @Override
    public void e(String msg, Throwable cause) {
        android.util.Log.e(tag, msg, cause);
    }
}
