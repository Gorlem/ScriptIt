package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import net.minecraft.client.MinecraftClient;

public class GameLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry namespace = registry.registerLibrary("game");

        namespace.registerVariable("fps", (name, minecraft) -> MinecraftClient.getCurrentFps());
        namespace.registerVariable("version", (name, minecraft) -> minecraft.getGame().getVersion().getName());
    }
}
