package com.ddoerr.scriptit.libraries.settings;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractSetting<T> implements Setting {
    protected final String name;
    protected final Supplier<T> getter;
    protected final Consumer<T> setter;

    public AbstractSetting(String name, Supplier<T> getter, Consumer<T> setter) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object get() {
        return getter.get();
    }
}
