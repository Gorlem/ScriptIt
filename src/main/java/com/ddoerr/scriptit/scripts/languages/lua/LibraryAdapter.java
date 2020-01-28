package com.ddoerr.scriptit.scripts.languages.lua;

import com.ddoerr.scriptit.api.libraries.FunctionExecutor;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.libraries.VariableUpdater;
import com.ddoerr.scriptit.scripts.languages.lua.LibraryAdapter.LuaFunction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Tickable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryAdapter extends TwoArgFunction implements Tickable {
    private NamespaceRegistry registry;
    private MinecraftClient minecraft;
    private LuaValue global;

    public LibraryAdapter(NamespaceRegistry registry) {
        this.registry = registry;
        minecraft = MinecraftClient.getInstance();
    }

    @Override
    public LuaValue call(LuaValue name, LuaValue global) {
        this.global = global;
        register(registry, global);
        return null;
    }

    @Override
    public void tick() {
        if (global == null)
            return;

        update(registry, global);
    }

    private void update(NamespaceRegistry registry, LuaValue luaValue) {
        LuaValue table = luaValue.get(registry.getName());

        if (!table.istable())
            table = tableOf();

        for (Map.Entry<String, VariableUpdater> variable : registry.getVariables().entrySet()) {
            Object value = variable.getValue().update(variable.getKey(), minecraft);
            table.set(variable.getKey(), ValueConverter.toLuaValue(value));
        }

        for (NamespaceRegistry namespace : registry.getNamespaces()) {
            update(namespace, table);
        }

        luaValue.set(registry.getName(), table);
    }

    private void register(NamespaceRegistry registry, LuaValue luaValue) {
        LuaValue table = luaValue.get(registry.getName());

        if (!table.istable())
            table = tableOf();

        for (Map.Entry<String, FunctionExecutor> function : registry.getFunctions().entrySet()) {
            table.set(function.getKey(), new LuaFunction(function.getKey(), function.getValue(), minecraft));
        }

        for (NamespaceRegistry namespace : registry.getNamespaces()) {
            register(namespace, table);
        }

        luaValue.set(registry.getName(), table);
    }

    static class LuaFunction extends VarArgFunction {
        private String name;
        private FunctionExecutor function;
        private MinecraftClient minecraft;

        public LuaFunction(String name, FunctionExecutor function, MinecraftClient minecraft) {
            this.name = name;
            this.function = function;
            this.minecraft = minecraft;
        }

        @Override
        public Varargs invoke(Varargs varargs) {
            List<Object> objects = new ArrayList<>();

            for (int i = 1; i <= varargs.narg(); i++) {
                LuaValue value = varargs.arg(i);
                Object object = ValueConverter.toObject(value);
                objects.add(object);
            }

            Object result = function.execute(name, minecraft, objects.toArray());

            return ValueConverter.toLuaValue(result);
        }
    }
}
