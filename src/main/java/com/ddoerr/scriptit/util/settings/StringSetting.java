package com.ddoerr.scriptit.util.settings;

import com.ddoerr.scriptit.util.ObjectConverter;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringSetting extends AbstractSetting {
    public StringSetting(String name, Supplier getter, Consumer setter) {
        super(name, getter, setter);
    }

    @Override
    public void set(Object object) {
        setter.accept(ObjectConverter.toString(object));
    }
}
