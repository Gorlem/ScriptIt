package com.ddoerr.scriptit.api.languages;

import com.ddoerr.scriptit.api.exceptions.ConversionException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface ContainedValue {
    default Object to(Type type) throws ConversionException {
        if (String.class.equals(type)) {
            return toString();
        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return toBoolean();
        } else if (Float.class.equals(type) || float.class.equals(type)) {
            return toFloat();
        } else if (Double.class.equals(type) || double.class.equals(type)) {
            return toDouble();
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            return toInteger();
        } else if (type instanceof Class<?> && Enum.class.isAssignableFrom((Class<?>)type)) {
            return toEnum((Class<? extends Enum>)type);
        } else if (type instanceof ParameterizedType && ((ParameterizedType)type).getRawType().equals(List.class)) {
            return toList(((ParameterizedType)type).getActualTypeArguments()[0]);
        } else if (type instanceof ParameterizedType && ((ParameterizedType)type).getRawType().equals(Map.class)) {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            return toMap(typeArguments[0], typeArguments[1]);
        } else if (ContainedValue.class.equals(type)) {
            return this;
        }

        throw new ConversionException("Unknown type " + type.getTypeName());
    }

    String format();

    String toString();
    boolean toBoolean();
    float toFloat();
    double toDouble();
    int toInteger();
    <T extends Enum<T>> T toEnum(Class<T> enumType);
    <K, V> Map<K, V> toMap(Type keyType, Type valueType) throws ConversionException;
    <T> List<T> toList(Type entryType) throws ConversionException;
}
