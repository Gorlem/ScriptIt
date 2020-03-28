package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;

public class DefaultLibraries implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        Resolver resolver = Resolver.getInstance();
        try {
            registry.registerLibrary("chat", resolver.create(ChatLibrary.class));registry.registerLibrary("game", resolver.create(GameLibrary.class));
            registry.registerLibrary("json", resolver.create(JsonLibrary.class));
            registry.registerLibrary("player", resolver.create(PlayerLibrary.class));
            registry.registerLibrary("scripts", resolver.create(ScriptsLibrary.class));
            registry.registerLibrary("server", resolver.create(ServerLibrary.class));
            registry.registerLibrary("gui", resolver.create(GuiLibrary.class));
            registry.registerLibrary("scoreboard", resolver.create(ScoreboardLibrary.class));
            registry.registerLibrary("options", resolver.create(OptionsLibrary.class));

            KeyboardLibrary keyboardLibrary = resolver.create(KeyboardLibrary.class);
            registry.registerLibrary("keyboard", keyboardLibrary);
            registry.registerTickable(keyboardLibrary);
        } catch (DependencyException e) {
            e.printStackTrace();
        }
    }
}
