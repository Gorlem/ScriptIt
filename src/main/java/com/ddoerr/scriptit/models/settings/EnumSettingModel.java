package com.ddoerr.scriptit.models.settings;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.exceptions.ConversionException;
import com.ddoerr.scriptit.api.languages.ContainedResultFactory;
import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.util.GenericEnumHelper;
import com.google.common.base.Joiner;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EnumSettingModel<E extends Enum<E>> extends AbstractSettingModel<E> implements ToggleableSettingModel<E> {
    private static final String VALUE_KEY = "value";
    private Class<E> enumClass;
    private Joiner joiner = Joiner.on(", ");

    public EnumSettingModel(String name, Class<E> enumClass, Supplier<E> getter, Consumer<E> setter) {
        super(name, getter, setter);
        this.enumClass = enumClass;
    }

    @Override
    public boolean hasGetter(String key) {
        return super.hasGetter(key) || VALUE_KEY.equals(key);
    }

    @Override
    public boolean hasSetter(String key) {
        return super.hasSetter(key) || VALUE_KEY.equals(key);
    }

    @Override
    public <T> T runGetter(String key, ContainedResultFactory<T> factory) {
        if (VALUE_KEY.equals(key)) {
            return factory.fromEnum(getValue());
        } else {
            return super.runGetter(key, factory);
        }
    }

    @Override
    public void runSetter(String key, ContainedValue value) {
        if (VALUE_KEY.equals(key)) {
            try {
                setValue(value.toEnum(enumClass));
            } catch (ConversionException e) {
                e.printStackTrace();
            }
        } else {
            super.runSetter(key, value);
        }
    }

    @Override
    public String getPossibleValues() {
        return joiner.join(Arrays.stream(GenericEnumHelper.getValues(enumClass)).map(Enum::name).collect(Collectors.toList()));
    }

    @Override
    public E getValue() {
        return super.getValue();
    }

    @Override
    public void setValue(E value) {
        super.setValue(value);
    }

    @Callable
    @Override
    public E toggle() {
        E next = GenericEnumHelper.getNext(enumClass, getValue());
        setValue(next);
        return next;
    }
}
