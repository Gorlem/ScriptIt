package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.Identifiable;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.api.util.DurationHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ContinuousTrigger implements Trigger {
    public static final Identifier IDENTIFIER = new Identifier(ScriptItMod.MOD_NAME, "duration");

    private int time;
    private ChronoUnit unit;

    private Duration duration;
    private Instant lastActivation = Instant.now();

    private Consumer<TriggerMessage> callback = message -> {};

    public ContinuousTrigger() {
        this.duration = Duration.ZERO;
    }

    public ContinuousTrigger(Duration durationBetweenActivations) {
        this.duration = durationBetweenActivations;
    }

    public ContinuousTrigger(int time, ChronoUnit unit) {
        this.time = time;
        this.unit = unit;
        this.duration = Duration.of(time, unit);
    }

    @Override
    public void close() {
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("time", Integer.toString(time));
        data.put("unit", unit.toString());
        return data;
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

    @Override
    public Identifier getIdentifier() {
        return null;
    }
}
