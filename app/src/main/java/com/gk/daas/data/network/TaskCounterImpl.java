package com.gk.daas.data.network;

import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * @author Gabor_Keszthelyi
 */
public class TaskCounterImpl implements TaskCounter {

    private final AtomicInteger onGoingTaskCounter = new AtomicInteger(0);

    private final Log log;

    private AllTasksFinishedListener listener;

    public TaskCounterImpl(LogFactory logFactory) {
        this.log = logFactory.create(getClass());
    }

    @Override
    public void taskStarted() {
        int noOfOngoingTasks = onGoingTaskCounter.incrementAndGet();
        log.d("Task counter increased to: " + noOfOngoingTasks);
    }

    @Override
    public void taskFinished() {
        int noOfOngoingTasks = onGoingTaskCounter.decrementAndGet();
        log.d("Task counter decreased to: " + noOfOngoingTasks);
        if (noOfOngoingTasks == 0) {
            log.d("No tasks left, schedule double-check.");
            scheduleNotifyListeners();
        }
    }

    // Schedule double-check for later to prevent shutting down while there is incoming data access request
    private void scheduleNotifyListeners() {
        Scheduler.Worker worker = Schedulers.io().createWorker();
        worker.schedule(
                () -> {
                    notifyListener();
                    worker.unsubscribe();
                },
                1, TimeUnit.SECONDS);
    }

    private void notifyListener() {
        if (onGoingTaskCounter.get() == 0) {
            log.d("No tasks left, notify listener.");
            listener.onAllTasksFinished();
        }
    }

    @Override
    public void setAllTaskFinishedListener(AllTasksFinishedListener listener) {
        this.listener = listener;
    }

}
