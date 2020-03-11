package com.ddoerr.scriptit.api.util;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GenericEnumHelper{
    public static <T extends Enum<T>> T[] getValues(Class<T> enumClass) {
        try {
            Method values = enumClass.getDeclaredMethod("values");
            Object object = values.invoke(null);
            return (T[])object;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return (T[])new Object[0];
    }

    public static <T extends Enum<T>> T getValue(Class<T> enumClass, int index) {
        return getValues(enumClass)[index];
    }

    public static <T extends Enum<T>> T getNext(Class<T> enumClass, T current) {
        T[] values = getValues(enumClass);
        int index = ArrayUtils.indexOf(values, current) + 1;
        int size = values.length;

        return values[index % size];
    }
}
