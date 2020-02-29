package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.mixin.FpsAccessor;
import net.minecraft.client.MinecraftClient;

public class GameLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry namespace = registry.registerLibrary("game");

        namespace.registerVariable("fps", (name, minecraft) -> FpsAccessor.getCurrentFps());
        namespace.registerVariable("version", (name, minecraft) -> minecraft.getGame().getVersion().getName());
    }
}
