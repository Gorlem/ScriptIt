package com.ddoerr.scriptit.util.settings;

import io.netty.util.internal.MathUtil;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractNumberSetting<T extends Number> extends AbstractSetting<T> {
    protected T minimum;
    protected T maximum;

    public AbstractNumberSetting(String name, Supplier<T> getter, Consumer<T> setter, T minimum, T maximum) {
        super(name, getter, setter);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    protected T clamp(T value) {
        if (value instanceof Integer) {
            return (T)(Number)MathHelper.clamp(value.intValue(), minimum.intValue(), maximum.intValue());
        } else if (value instanceof Double) {
            return (T)(Number)MathHelper.clamp(value.doubleValue(), minimum.doubleValue(), maximum.doubleValue());
        } else if (value instanceof Float) {
            return (T)(Number)MathHelper.clamp(value.floatValue(), minimum.floatValue(), maximum.floatValue());
        }
        return (T)(Number)0;
    }
}
