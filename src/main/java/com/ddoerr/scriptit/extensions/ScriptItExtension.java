package com.ddoerr.scriptit.extensions;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.registry.*;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.elements.IconHudElement;
import com.ddoerr.scriptit.elements.TextHudElement;
import com.ddoerr.scriptit.events.GameConnectEvent;
import com.ddoerr.scriptit.events.ChatIncomingEvent;
import com.ddoerr.scriptit.events.ChatOutgoingEvent;
import com.ddoerr.scriptit.events.SoundEvent;
import com.ddoerr.scriptit.languages.lua.LuaLanguage;
import com.ddoerr.scriptit.libraries.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.SimpleRegistry;

public class ScriptItExtension implements ExtensionInitializer {

    private Resolver resolver;

    @Override
    public void onInitialize(ScriptItRegistry registry, Resolver resolver) {
        this.resolver = resolver;
        add(registry.languages, "lua", LuaLanguage.class);

        add(registry.libraries, "scripts", ScriptsLibrary.class);
        add(registry.libraries, "json", JsonLibrary.class);

        add(registry.libraries, "chat", ChatLibrary.class);
        add(registry.libraries, "game", GameLibrary.class);
        add(registry.libraries, "gui", GuiLibrary.class);
        add(registry.libraries, "keyboard", KeyboardLibrary.class);
        add(registry.libraries, "options", OptionsLibrary.class);
        add(registry.libraries, "player", PlayerLibrary.class);
        add(registry.libraries, "scoreboard", ScoreboardLibrary.class);
        add(registry.libraries, "server", ServerLibrary.class);
        add(registry.libraries, "shared", SharedLibrary.class);

        add(registry.hudElements, "text", TextHudElement.class);
        add(registry.hudElements, "icon", IconHudElement.class);

        add(registry.events, "game/connect", GameConnectEvent.class);
        add(registry.events, "chat/incoming", ChatIncomingEvent.class);
        add(registry.events, "chat/outgoing", ChatOutgoingEvent.class);
        add(registry.events, "sound", SoundEvent.class);
    }

    private <T> void add(MutableRegistry<T> registry, String identifierPath, Class<? extends T> type) {
        try {
            registry.add(new Identifier(ScriptItMod.MOD_NAME, identifierPath), resolver.create(type));
        } catch (DependencyException e) {
            e.printStackTrace();
        }
    }
}
