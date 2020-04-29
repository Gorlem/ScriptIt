package com.ddoerr.scriptit.languages.lua;

import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.exceptions.ConversionException;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuaContainedValue implements ContainedValue {
    public static <T> T convert(Type type, LuaValue value) throws ConversionException {
        return (T) new LuaContainedValue(value).to(type);
    }

    private LuaValue value;

    public LuaContainedValue(LuaValue luaValue) {
        this.value = luaValue;
    }

    public LuaValue getValue() {
        return value;
    }

    @Override
    public Type guessType() {
        if (value.isboolean()) {
            return Boolean.class;
        } else if (value.isint()) {
            return Integer.class;
        } else if (value.isnumber()) {
            return Double.class;
        } else if (value.isstring()) {
            return String.class;
        } else if (value.istable()) {
            return Map.class;
        }

        return Object.class;
    }

    @Override
    public String format() {
        return value.tojstring();
    }

    @Override
    public String toStr() throws ConversionException {
        try {
            return value.checkjstring();
        } catch (LuaError e) {
            throw new ConversionException("Cannot convert to String");
        }
    }

    @Override
    public boolean toBoolean() throws ConversionException {
        try {
            return value.checkboolean();
        } catch (Exception e) {
            throw new ConversionException("Cannot convert to Boolean");
        }
    }

    @Override
    public float toFloat() throws ConversionException {
        try {
            return (float) value.checkdouble();
        } catch (Exception e) {
            throw new ConversionException("Cannot convert to Float");
        }
    }

    @Override
    public double toDouble() throws ConversionException {
        try {
            return value.checkdouble();
        } catch (Exception e) {
            throw new ConversionException("Cannot convert to Double");
        }
    }

    @Override
    public int toInteger() throws ConversionException {
        try {
            return value.checkint();
        } catch (Exception e) {
            throw new ConversionException("Cannot convert to Integer");
        }
    }

    @Override
    public <T extends Enum<T>> T toEnum(Class<T> enumType) throws ConversionException {
        try {
            return Enum.valueOf(enumType, value.checkjstring());
        } catch (Exception e) {
            throw new ConversionException("Cannot convert to Enum");
        }
    }

    @Override
    public <K, V> Map<K, V> toMap(Type keyType, Type valueType) throws ConversionException {
        try {
            Map<K, V> map = new HashMap<>();

            LuaValue counter = LuaValue.NIL;
            Varargs key;
            while (true) {
                key = value.next(counter);
                counter = key.arg(1);

                if (counter == LuaValue.NIL)
                    break;

                map.put(convert(keyType, counter), convert(valueType, key.arg(2)));
            }

            return map;
        } catch (ConversionException e) {
            throw new ConversionException("Cannot convert to Map");
        }
    }

    @Override
    public <T> List<T> toList(Type entryType) throws ConversionException {
        try {
            List<T> list = new ArrayList<>(value.len().toint());

            LuaValue counter = LuaValue.NIL;
            Varargs key;
            while (true) {
                key = value.next(counter);
                counter = key.arg(1);

                if (counter == LuaValue.NIL)
                    break;

                list.add(counter.toint() - 1, convert(entryType, key.arg(2)));
            }

            return list;
        } catch (LuaError e) {
            throw new ConversionException("Cannot convert to List");
        }
    }
}
