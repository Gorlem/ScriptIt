package com.ddoerr.scriptit.api.libraries;

import net.minecraft.client.MinecraftClient;

public interface FunctionExecutor {
    Object execute(String name, MinecraftClient minecraft, Object... arguments);
}
