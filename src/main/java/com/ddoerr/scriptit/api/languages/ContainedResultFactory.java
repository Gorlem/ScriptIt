package com.ddoerr.scriptit.api.languages;

import com.ddoerr.scriptit.api.exceptions.ConversionException;
import com.ddoerr.scriptit.api.libraries.Model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface ContainedResultFactory<T> {
    default T from(Type type, Object value) throws ConversionException {
        if (value == null) {
            return nullValue();
        } else if (String.class.equals(type)) {
            return fromString((String)value);
        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return fromBoolean((boolean)value);
        } else if (Float.class.equals(type) || float.class.equals(type)) {
            return fromFloat((float)value);
        } else if (Double.class.equals(type) || double.class.equals(type)) {
            return fromDouble((double)value);
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            return fromInteger((int)value);
        } else if (type instanceof Class<?> && Enum.class.isAssignableFrom((Class<?>)type)) {
            return fromEnum((Enum<?>)value);
        } else if (type instanceof ParameterizedType && ((ParameterizedType)type).getRawType().equals(List.class)) {
            return fromList(((ParameterizedType)type).getActualTypeArguments()[0], (List<?>)value);
        } else if (type instanceof ParameterizedType && ((ParameterizedType)type).getRawType().equals(Map.class)) {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            return fromMap(typeArguments[0], typeArguments[1], (Map<?, ?>)value);
        } else if (type instanceof Class<?> && Model.class.isAssignableFrom((Class<?>)type)) {
            return fromModel((Model)value);
        } else if (Void.TYPE.equals(type)) {
            return nullValue();
        } else if (Object.class.equals(type)) {
            return from(value.getClass(), value);
        } else if (ContainedValue.class.equals(type)) {
            return fromContainedValue((ContainedValue)value);
        }

        throw new ConversionException("Unknown type " + type.getTypeName());
    }

    T nullValue();

    T fromString(String value);
    T fromBoolean(boolean value);
    T fromFloat(float value);
    T fromDouble(double value);
    T fromInteger(int value);
    T fromEnum(Enum<?> value);
    T fromMap(Type keyType, Type valueType, Map<?, ?> value) throws ConversionException;
    T fromList(Type entryType, List<?> value) throws ConversionException;
    T fromModel(Model value);
    default T fromContainedValue(ContainedValue containedValue) throws ConversionException {
        Type typeGuess = containedValue.guessType();
        return from(typeGuess, containedValue.to(typeGuess));
    }
}
