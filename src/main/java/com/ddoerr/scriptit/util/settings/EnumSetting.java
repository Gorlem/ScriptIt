package com.ddoerr.scriptit.util.settings;

import com.ddoerr.scriptit.util.ObjectConverter;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnumSetting<T extends Enum<T>> extends AbstractSetting<T> {
    private Class<T> enumClass;

    public EnumSetting(String name, Class<T> enumClass, Supplier<T> getter, Consumer<T> setter) {
        super(name, getter, setter);
        this.enumClass = enumClass;
    }

    @Override
    public Object get() {
        return super.get().toString();
    }

    @Override
    public void set(Object object) {
        setter.accept(ObjectConverter.toEnum(enumClass, object));
    }
}
