package com.gk.daas.data.network;

import com.gk.daas.core.Config;
import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * @author Gabor_Keszthelyi
 */
public class TaskCounterImpl implements TaskCounter {

    private final AtomicInteger onGoingTaskCounter = new AtomicInteger(0);

    private final Log log;

    private TasksFinishedListener listener;

    private Subscription timeoutSubscription;

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
            log.d("No tasks left, schedule double-check after timeout.");
            scheduleNotifyListeners();
        }
    }

    private void scheduleNotifyListeners() {
        // Note: Maybe this could be optimized to not create a new Observable every time.
        if (isOngoing(timeoutSubscription)) {
            timeoutSubscription.unsubscribe();
        }
        timeoutSubscription = Observable
                .timer(Config.BACKGROUND_SERVICE_SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS, Schedulers.io())
                .subscribe(aLong -> notifyListener());
    }

    private boolean isOngoing(Subscription subscription) {
        return subscription != null && !subscription.isUnsubscribed();
    }

    private void notifyListener() {
        if (onGoingTaskCounter.get() == 0) {
            log.d("No tasks left after timeout, notify listener.");
            listener.onIdleTimeEnded();
        }
    }

    @Override
    public void setAllTaskFinishedListener(TasksFinishedListener listener) {
        this.listener = listener;
    }

}
