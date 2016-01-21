package com.gk.daas.data.network;


import android.os.Handler;

/**
 * @author Gabor_Keszthelyi
 */
public interface TaskCounter {

    void setAllTaskFinishedListener(AllTasksFinishedListener listener);

    /**
     * @param handler to use for post with delay
     */
    void setHandler(Handler handler);

    void taskStarted();

    void taskFinished();

    interface AllTasksFinishedListener {

        void onAllTasksFinished();
    }
}
