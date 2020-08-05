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
        return value;
    }

    @Override
    public String toStr() throws ConversionException {
        return value;
    }

    @Override
    public boolean toBoolean() throws ConversionException {
        return Boolean.parseBoolean(value);
    }

    @Override
    public float toFloat() throws ConversionException {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            throw new ConversionException("Cannot convert to Float");
        }
    }

    @Override
    public double toDouble() throws ConversionException {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            throw new ConversionException("Cannot convert to Double");
        }
    }

    @Override
    public int toInteger() throws ConversionException {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new ConversionException("Cannot convert to Integer");
        }
    }

    @Override
    public <T extends Enum<T>> T toEnum(Class<T> enumType) throws ConversionException {
        try {
            return Enum.valueOf(enumType, value);
        } catch (Exception e) {
            throw new ConversionException("Cannot convert to Enum");
        }
    }

    @Override
    public <K, V> Map<K, V> toMap(Type keyType, Type valueType) throws ConversionException {
        throw new ConversionException("Cannot convert to Map");
    }

    @Override
    public <T> List<T> toList(Type entryType) throws ConversionException {
        throw new ConversionException("Cannot convert to List");
    }
}
