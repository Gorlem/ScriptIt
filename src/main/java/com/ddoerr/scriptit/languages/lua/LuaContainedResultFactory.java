package com.ddoerr.scriptit.languages.lua;

import com.ddoerr.scriptit.api.languages.ContainedResultFactory;
import com.ddoerr.scriptit.api.exceptions.ConversionException;
import com.ddoerr.scriptit.api.libraries.Model;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LuaContainedResultFactory implements ContainedResultFactory<LuaValue> {
    private Converter<String, String> caseConverter = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);

    @Override
    public LuaValue nullValue() {
        return LuaValue.NIL;
    }

    @Override
    public LuaValue fromString(String value) {
        return LuaValue.valueOf(value);
    }

    @Override
    public LuaValue fromBoolean(boolean value) {
        return LuaValue.valueOf(value);
    }

    @Override
    public LuaValue fromFloat(float value) {
        return LuaValue.valueOf(value);
    }

    @Override
    public LuaValue fromDouble(double value) {
        return LuaValue.valueOf(value);
    }

    @Override
    public LuaValue fromInteger(int value) {
        return LuaValue.valueOf(value);
    }

    @Override
    public LuaValue fromEnum(Enum<?> value) {
        return LuaValue.valueOf(value.name());
    }

    @Override
    public LuaValue fromMap(Type keyType, Type valueType, Map<?, ?> value) throws ConversionException {
        LuaTable table = LuaValue.tableOf();

        for (Map.Entry<?, ?> entry : value.entrySet()) {
            table.set(from(keyType, entry.getKey()), from(valueType, entry.getValue()));
        }

        return table;
    }

    @Override
    public LuaValue fromList(Type entryType, List<?> value) throws ConversionException {
        LuaTable list = LuaValue.tableOf();

        for (Object entry : value) {
            list.insert(0, from(entryType, entry));
        }

        return list;
    }

    @Override
    public LuaValue fromModel(Model value) {
        LuaTable table = LuaValue.tableOf();
        LuaValue metaTable = LuaValue.tableOf();

        metaTable.set("__index", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue table, LuaValue luaKey) {
                String key = caseConverter.convert(luaKey.tojstring());

                if (value.hasGetter(key)) {
                    return value.runGetter(key, LuaContainedResultFactory.this);
                }

                if (value.hasFunction(key)) {
                    return new VarArgFunction() {
                        @Override
                        public Varargs invoke(Varargs varargs) {
                            List<LuaContainedValue> arguments = new ArrayList<>();
                            for (int i = 1; i <= varargs.narg(); i++) {
                                arguments.add(new LuaContainedValue(varargs.arg(i)));
                            }
                            return value.runFunction(key, arguments.toArray(new LuaContainedValue[0]), LuaContainedResultFactory.this);
                        }
                    };
                }

                return LuaValue.NIL;
            }
        });

        metaTable.set("__newindex", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue table, LuaValue luaKey, LuaValue luaValue) {
                String key = caseConverter.convert(luaKey.tojstring());

                if (value.hasSetter(key)) {
                    value.runSetter(key, new LuaContainedValue(luaValue));
                }

                return LuaValue.NIL;
            }
        });

        table.setmetatable(metaTable);
        return table;
    }
}
