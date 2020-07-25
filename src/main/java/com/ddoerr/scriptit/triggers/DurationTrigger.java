package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.fields.Field;
import com.ddoerr.scriptit.fields.IntegerField;
import com.ddoerr.scriptit.fields.SelectionField;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DurationTrigger extends AbstractTrigger {
    public static final Identifier IDENTIFIER = new Identifier(ScriptItMod.MOD_NAME, "duration");
    public static final String TIME_FIELD = "time";
    public static final String UNIT_FIELD = "unit";

    private Duration duration = Duration.ZERO;
    private Instant lastActivation = Instant.now();

    private IntegerField timeField;
    private SelectionField unitField;

    public DurationTrigger() {
        timeField = new IntegerField();
        timeField.setTitle(new LiteralText("Time"));
        timeField.setDescription(new LiteralText("Amount of time"));
        timeField.setValue(0);
        fields.put(TIME_FIELD, timeField);

        unitField = new SelectionField();
        unitField.setTitle(new LiteralText("Unit"));
        unitField.setDescription(new LiteralText("Time unit"));
        unitField.setValue(ChronoUnit.MILLIS.name());

        List<String> units = Stream.of(ChronoUnit.MILLIS, ChronoUnit.SECONDS, ChronoUnit.MINUTES, ChronoUnit.HOURS)
                .map(Enum::name).collect(Collectors.toList());
        unitField.setValues(units);

        fields.put(UNIT_FIELD, unitField);
    }

    @Override
    public void start() {
        int time = timeField.getValue();
        ChronoUnit unit = ChronoUnit.valueOf(unitField.getValue());
        duration = Duration.of(time, unit);
    }

    @Override
    public void check() {
        Duration between = Duration.between(lastActivation, Instant.now());

        if (duration.compareTo(between) < 0) {
            callback.accept(new TriggerMessageImpl());
            lastActivation = Instant.now();
        }
    }

    @Override
    public String toString() {
        return "every " + timeField.getValue() + " " + unitField.getValue();
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }
}
