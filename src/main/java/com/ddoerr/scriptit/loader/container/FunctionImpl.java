package com.ddoerr.scriptit.loader.container;

import com.ddoerr.scriptit.api.libraries.Function;
import com.ddoerr.scriptit.api.libraries.FunctionExecutor;
import net.minecraft.client.MinecraftClient;

public class FunctionImpl implements Function {
    private String name;
    private FunctionExecutor executor;
    private boolean isDisabled = false;

    public FunctionImpl(String name, FunctionExecutor executor) {
        this.name = name;
        this.executor = executor;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isDisabled() {
        return isDisabled;
    }

    @Override
    public void disable() {
        isDisabled = true;
    }

    @Override
    public Object execute(String name, MinecraftClient minecraft, Object... arguments) {
        return executor.execute(name, minecraft, arguments);
    }
}
