package com.ddoerr.scriptit.libraries.settings;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractNumberSettingModel<T extends Number> extends AbstractSettingModel<T> implements ToggleableSettingModel<T> {
    protected T minimum;
    protected T maximum;
    protected T step;

    public AbstractNumberSettingModel(String name, Supplier<T> getter, Consumer<T> setter, T minimum, T maximum, T step) {
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
