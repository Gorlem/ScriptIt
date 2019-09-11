package com.ddoerr.scriptit.scripts.languages.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.*;

public class ValueConverter {
    public static LuaValue toLuaValue(Object value) {
        if (value instanceof String) {
            return LuaValue.valueOf((String)value);
        }

        if (value instanceof Integer) {
            return LuaValue.valueOf((Integer)value);
        }

        if (value instanceof Boolean) {
            return LuaValue.valueOf((boolean)value);
        }

        if (value instanceof Float) {
            return LuaValue.valueOf((float)value);
        }

        if (value instanceof Double) {
            return LuaValue.valueOf((double)value);
        }

        if (value instanceof Iterable) {
            LuaTable list = LuaValue.tableOf();

            for ( Object entry : (Iterable)value ) {
                list.insert(0, toLuaValue(entry));
            }

            return list;
        }

        if (value instanceof Map) {
            LuaTable map = LuaValue.tableOf();

            for ( Map.Entry<Object, Object> entry : ((Map<Object, Object>) value).entrySet() ) {
                map.set(entry.getKey().toString(), toLuaValue(entry.getValue()));
            }

            return map;
        }

        return LuaValue.NIL;
    }

    public static Object toObject(LuaValue value) {
        if (value.isstring()) {
            return value.tojstring();
        }

        if (value.isint()) {
            return value.toint();
        }

        if (value.isboolean()) {
            return value.toboolean();
        }

        if (value.isnumber()) {
            return value.tofloat();
        }

        if (value.istable()) {
            LuaValue[] keys = value.checktable().keys();

            boolean allIntKeys = Arrays.stream(keys).allMatch(lua -> lua.isint());

            if (allIntKeys && keys.length > 0) {
                List<Object> list = new ArrayList<>(value.len().toint());

                LuaValue counter = LuaValue.NIL;
                Varargs key;
                while (true) {
                    key = value.next(counter);
                    counter = key.arg(1);

                    if (counter == LuaValue.NIL)
                        break;

                    list.add(counter.toint() - 1, toObject(key.arg(2)));
                }

                return list;
            }
            else {
                Map<String, Object> map = new HashMap<>();

                LuaValue counter = LuaValue.NIL;
                Varargs key;
                while (true) {
                    key = value.next(counter);
                    counter = key.arg(1);

                    if (counter == LuaValue.NIL)
                        break;

                    map.put(counter.tojstring(), toObject(key.arg(2)));
                }

                return map;
            }
        }

        return null;
    }
}
