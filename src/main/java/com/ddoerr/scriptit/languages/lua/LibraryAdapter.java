package com.ddoerr.scriptit.languages.lua;

import com.ddoerr.scriptit.api.libraries.Function;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.libraries.Variable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Tickable;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaNil;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import java.util.ArrayList;
import java.util.List;

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

        for (Variable variable : registry.getVariables()) {
            if (variable.isDisabled()) {
                continue;
            }

            try {
                Object value = variable.update(variable.getName(), minecraft);
                table.set(variable.getName(), ValueConverter.toLuaValue(value));
            } catch (Exception e) {
                variable.disable();
                e.printStackTrace();
//                throw new LuaError("An error occurred while updating `" + variable.getName() + "`. Variable will be disabled until the game is restarted.");
            }
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

        for (Function function : registry.getFunctions()) {
            table.set(function.getName(), new LuaFunction(function, minecraft));
        }

        for (NamespaceRegistry namespace : registry.getNamespaces()) {
            register(namespace, table);
        }

        luaValue.set(registry.getName(), table);
    }

    static class LuaFunction extends VarArgFunction {
        private Function function;
        private MinecraftClient minecraft;

        public LuaFunction(Function function, MinecraftClient minecraft) {
            this.function = function;
            this.minecraft = minecraft;
        }

        @Override
        public Varargs invoke(Varargs varargs) {
            if (function.isDisabled()) {
                return LuaNil.NIL;
            }

            List<Object> objects = new ArrayList<>();

            for (int i = 1; i <= varargs.narg(); i++) {
                LuaValue value = varargs.arg(i);
                Object object = ValueConverter.toObject(value);
                objects.add(object);
            }

            try {
                Object result = function.execute(function.getName(), minecraft, objects.toArray());
                return ValueConverter.toLuaValue(result);
            } catch (Exception e) {
                function.disable();
                e.printStackTrace();
                throw new LuaError("An error occurred while executing `" + function.getName() + "`. Function will be disabled until the game is restarted.");
            }
        }
    }
}
