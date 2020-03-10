package com.ddoerr.scriptit.libraries.settings;

import com.ddoerr.scriptit.api.util.GenericEnumHelper;
import com.ddoerr.scriptit.api.util.ObjectConverter;
import com.google.common.base.Joiner;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EnumSetting<T extends Enum<T>> extends AbstractSetting<T> implements ToggleableSetting {
    private Class<T> enumClass;
    private Joiner joiner = Joiner.on(", ");

    public EnumSetting(String name, Class<T> enumClass, Supplier<T> getter, Consumer<T> setter) {
        super(name, getter, setter);
        this.enumClass = enumClass;
    }

    @Override
    public Object get() {
        return getter.get().name();
    }

    @Override
    public void set(Object object) {
        setter.accept(ObjectConverter.toEnum(enumClass, object));
    }

    @Override
    public String getPossibleValues() {
        return joiner.join(Arrays.stream(GenericEnumHelper.getValues(enumClass)).map(Enum::name).collect(Collectors.toList()));
    }

    @Override
    public Object toggle() {
        T next = GenericEnumHelper.getNext(enumClass, getter.get());
        setter.accept(next);
        return next;
    }
}
