package ml.gorlem.scriptit.scripts.libraries;

import ml.gorlem.scriptit.api.libraries.LibraryInitializer;
import ml.gorlem.scriptit.api.libraries.LibraryRegistry;
import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import net.minecraft.client.MinecraftClient;

public class GameLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry namespace = registry.registerLibrary("game");

        namespace.registerVariable("fps", (name, minecraft) -> MinecraftClient.getCurrentFps());
        namespace.registerVariable("version", (name, minecraft) -> minecraft.getGame().getVersion().getName());
    }
}
