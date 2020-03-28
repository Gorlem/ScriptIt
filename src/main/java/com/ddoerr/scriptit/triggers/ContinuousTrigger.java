package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.libraries.Library;
import com.ddoerr.scriptit.api.util.DurationHelper;
import net.minecraft.util.Pair;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

        return duration.compareTo(between) < 0;
    }

    @Override
    public void reset() {
        lastActivation = Instant.now();
    }

    @Override
    public Library getAdditionalLibrary() {
        return null;
    }

    @Override
    public void close() {

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
