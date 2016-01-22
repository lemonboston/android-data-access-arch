package com.gk.daas.data.network;


/**
 * @author Gabor_Keszthelyi
 */
public interface TaskCounter {

    void setAllTaskFinishedListener(AllTasksFinishedListener listener);

    void taskStarted();

    void taskFinished();

    interface AllTasksFinishedListener {

        void onAllTasksFinished();
    }
}
