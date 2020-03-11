package com.ddoerr.scriptit.libraries.settings;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractNumberSetting<T extends Number> extends AbstractSetting<T> implements ToggleableSetting {
    protected T minimum;
    protected T maximum;
    protected T step;

    public AbstractNumberSetting(String name, Supplier<T> getter, Consumer<T> setter, T minimum, T maximum, T step) {
        super(name, getter, setter);
        this.minimum = minimum;
        this.maximum = maximum;
        this.step = step;
    }

    @Override
    public String getPossibleValues() {
        return minimum.toString() + " - " + maximum.toString() + "; step = " + step.toString();
    }
}
