package com.ddoerr.scriptit.api.util;

import net.minecraft.util.Pair;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

public class DurationHelper {
    private static Map<ChronoUnit, Integer> units = new LinkedHashMap<>();
    static {
        units.put(ChronoUnit.HOURS, 60 * 60);
        units.put(ChronoUnit.MINUTES, 60);
        units.put(ChronoUnit.SECONDS, 1);
    }

    public static Pair<ChronoUnit, Long> getUnitAndAmount(Duration duration) {
        long seconds = duration.getSeconds();
        long nanos = duration.getNano();

        if (nanos == 0 && seconds != 0) {
            for (Map.Entry<ChronoUnit, Integer> unit : units.entrySet()) {
                if (seconds % unit.getValue() == 0) {
                    return new Pair<>(unit.getKey(), seconds / unit.getValue());
                }
            }
        }

        return new Pair<>(ChronoUnit.MILLIS, duration.toMillis());
    }
}
