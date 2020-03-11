package com.ddoerr.scriptit.loader.container;

import com.ddoerr.scriptit.api.libraries.Variable;
import com.ddoerr.scriptit.api.libraries.VariableUpdater;
import net.minecraft.client.MinecraftClient;

public class VariableImpl implements Variable {
    private String name;
    private VariableUpdater updater;
    private boolean isDisabled = false;

    public VariableImpl(String name, VariableUpdater updater) {
        this.name = name;
        this.updater = updater;
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
    public Object update(String name, MinecraftClient minecraft) {
        return updater.update(name, minecraft);
    }
}
