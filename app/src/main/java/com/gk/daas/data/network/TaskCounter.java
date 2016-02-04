package com.gk.daas.data.network;


/**
 * @author Gabor_Keszthelyi
 */
public interface TaskCounter {

    void setAllTaskFinishedListener(TasksFinishedListener listener);

    void taskStarted();

    void taskFinished();

    interface TasksFinishedListener {

        void onIdleTimeEnded();
    }
}
