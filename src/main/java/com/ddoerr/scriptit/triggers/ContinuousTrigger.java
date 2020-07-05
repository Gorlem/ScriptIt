package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.api.util.DurationHelper;
import net.minecraft.util.Pair;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;

public class ContinuousTrigger implements Trigger {
    private Duration duration;
    private Instant lastActivation = Instant.now();

    private Consumer<TriggerMessage> callback = message -> {};

    public ContinuousTrigger() {
        this.duration = Duration.ZERO;
    }

    public ContinuousTrigger(Duration durationBetweenActivations) {
        this.duration = durationBetweenActivations;
    }

    @Override
    public void close() {
    }

    @Override
    public void setCallback(Consumer<TriggerMessage> callback) {
        this.callback = callback;
    }

    @Override
    public void check() {
        Duration between = Duration.between(lastActivation, Instant.now());

        if (duration.compareTo(between) < 0) {
            callback.accept(new TriggerMessageImpl());
            lastActivation = Instant.now();
        }
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        Pair<ChronoUnit, Long> unitAndAmount = DurationHelper.getUnitAndAmount(duration);
        return "every " + unitAndAmount.getRight().toString() + " " + unitAndAmount.getLeft().toString();
    }
}
