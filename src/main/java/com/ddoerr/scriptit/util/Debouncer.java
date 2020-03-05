package com.ddoerr.scriptit.util;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Debouncer {
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    Future<?> future;
    Duration duration;
    Runnable runnable;

    public Debouncer(Duration duration, Runnable runnable) {
        this.duration = duration;
        this.runnable = runnable;
    }

    public void call() {
        if (future != null) {
            future.cancel(true);
        }

        future = scheduler.schedule(runnable, duration.toMillis(), TimeUnit.MILLISECONDS);
    }
}
