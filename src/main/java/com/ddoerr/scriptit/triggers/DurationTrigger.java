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

public class DurationTrigger implements Trigger {
    public static final Identifier IDENTIFIER = new Identifier(ScriptItMod.MOD_NAME, "duration");
    public static final String TIMENAME = "time";
    public static final String UNITNAME = "unit";

    private int time = 0;
    private ChronoUnit unit = ChronoUnit.MILLIS;

    private Duration duration = Duration.ZERO;
    private Instant lastActivation = Instant.now();

    private Consumer<TriggerMessage> callback = message -> {};

    @Override
    public void close() { }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put(TIMENAME, Integer.toString(time));
        data.put(UNITNAME, unit.name());
        return data;
    }

    @Override
    public void setData(Map<String, String> data) {
        setTime(Integer.parseInt(data.get(TIMENAME)));
        setUnit(ChronoUnit.valueOf(data.get(UNITNAME)));
    }

    public void setTime(int time) {
        this.time = time;
        this.duration = Duration.of(time, unit);
    }

    public void setUnit(ChronoUnit unit) {
        this.unit = unit;
        this.duration = Duration.of(time, unit);
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

    public ChronoUnit getUnit() {
        return unit;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "every " + time + " " + unit;
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }
}
