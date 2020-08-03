package com.ddoerr.scriptit.extension.text;

import com.ddoerr.scriptit.api.exceptions.ConversionException;
import com.ddoerr.scriptit.api.languages.ContainedValue;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TextContainedValue implements ContainedValue {
    private String value;

    public TextContainedValue(String value) {
        this.value = value;
    }

    @Override
    public Type guessType() {
        return String.class;
    }

    @Override
    public String format() {
        return value.toString();
    }

    @Override
    public String toStr() throws ConversionException {
        return value;
    }

    @Override
    public boolean toBoolean() throws ConversionException {
        throw new ConversionException();
    }

    @Override
    public float toFloat() throws ConversionException {
        throw new ConversionException();
    }

    @Override
    public double toDouble() throws ConversionException {
        throw new ConversionException();
    }

    @Override
    public int toInteger() throws ConversionException {
        throw new ConversionException();
    }

    @Override
    public <T extends Enum<T>> T toEnum(Class<T> enumType) throws ConversionException {
        throw new ConversionException();
    }

    @Override
    public <K, V> Map<K, V> toMap(Type keyType, Type valueType) throws ConversionException {
        throw new ConversionException();
    }

    @Override
    public <T> List<T> toList(Type entryType) throws ConversionException {
        throw new ConversionException();
    }
}
