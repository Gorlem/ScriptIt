package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;

import java.time.Duration;
import java.time.Instant;

public class ContinuousTrigger implements Trigger {
    Duration duration;
    Instant lastActivation = Instant.now();

    public ContinuousTrigger() {
        this.duration = Duration.ZERO;
    }

    public ContinuousTrigger(Duration durationBetweenActivations) {
        this.duration = durationBetweenActivations;
    }

    @Override
    public boolean canRun() {
        Duration between = Duration.between(lastActivation, Instant.now());

        if (duration.compareTo(between) < 0) {
            return true;
        }

        return false;
    }

    @Override
    public void reset() {
        lastActivation = Instant.now();
    }

    @Override
    public NamespaceRegistry additionalRegistry() {
        return null;
    }

    @Override
    public void close() {

    }

    public Duration getDuration() {
        return duration;
    }
}
