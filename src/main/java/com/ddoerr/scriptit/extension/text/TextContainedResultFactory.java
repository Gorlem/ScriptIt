package com.ddoerr.scriptit.extension.text;

import com.ddoerr.scriptit.api.exceptions.ConversionException;
import com.ddoerr.scriptit.api.languages.ContainedResultFactory;
import com.ddoerr.scriptit.api.libraries.Model;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TextContainedResultFactory implements ContainedResultFactory<Object> {
    @Override
    public Object nullValue() {
        return null;
    }

    @Override
    public Object fromString(String value) {
        return value;
    }

    @Override
    public Object fromBoolean(boolean value) {
        return value;
    }

    @Override
    public Object fromFloat(float value) {
        return value;
    }

    @Override
    public Object fromDouble(double value) {
        return value;
    }

    @Override
    public Object fromInteger(int value) {
        return value;
    }

    @Override
    public Object fromEnum(Enum<?> value) {
        return value;
    }

    @Override
    public Object fromMap(Type keyType, Type valueType, Map<?, ?> value) throws ConversionException {
        return value;
    }

    @Override
    public Object fromList(Type entryType, List<?> value) throws ConversionException {
        return value;
    }

    @Override
    public Object fromModel(Model value) {
        return value;
    }
}
