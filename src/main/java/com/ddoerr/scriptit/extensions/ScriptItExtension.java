package com.ddoerr.scriptit.extensions;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.registry.*;
import com.ddoerr.scriptit.elements.IconHudElement;
import com.ddoerr.scriptit.elements.TextHudElement;
import com.ddoerr.scriptit.events.GameConnectEvent;
import com.ddoerr.scriptit.events.ChatIncomingEvent;
import com.ddoerr.scriptit.events.ChatOutgoingEvent;
import com.ddoerr.scriptit.events.SoundEvent;
import com.ddoerr.scriptit.languages.lua.LuaLanguage;
import com.ddoerr.scriptit.libraries.*;
import net.minecraft.util.Identifier;

public class ScriptItExtension implements ExtensionInitializer {

    @Override
    public void onInitialize(ScriptItRegistry registry, Resolver resolver) {
        try {
            registry.languages.add(new Identifier(ScriptItMod.MOD_NAME, "lua"), resolver.create(LuaLanguage.class));

            registry.libraries.add(new Identifier(ScriptItMod.MOD_NAME, "scripts"), resolver.create(ScriptsLibrary.class));
            registry.libraries.add(new Identifier(ScriptItMod.MOD_NAME, "json"), resolver.create(JsonLibrary.class));

            registry.libraries.add(new Identifier(ScriptItMod.MOD_NAME, "chat"), resolver.create(ChatLibrary.class));
            registry.libraries.add(new Identifier(ScriptItMod.MOD_NAME, "game"), resolver.create(GameLibrary.class));
            registry.libraries.add(new Identifier(ScriptItMod.MOD_NAME, "gui"), resolver.create(GuiLibrary.class));
            registry.libraries.add(new Identifier(ScriptItMod.MOD_NAME, "keyboard"), resolver.create(KeyboardLibrary.class));
            registry.libraries.add(new Identifier(ScriptItMod.MOD_NAME, "options"), resolver.create(OptionsLibrary.class));
            registry.libraries.add(new Identifier(ScriptItMod.MOD_NAME, "player"), resolver.create(PlayerLibrary.class));
            registry.libraries.add(new Identifier(ScriptItMod.MOD_NAME, "scoreboard"), resolver.create(ScoreboardLibrary.class));
            registry.libraries.add(new Identifier(ScriptItMod.MOD_NAME, "server"), resolver.create(ServerLibrary.class));

            registry.hudElements.add(new Identifier(ScriptItMod.MOD_NAME, "text"), resolver.create(TextHudElement.class));
            registry.hudElements.add(new Identifier(ScriptItMod.MOD_NAME, "icon"), resolver.create(IconHudElement.class));

            registry.events.add(new Identifier(ScriptItMod.MOD_NAME, "game/connect"), resolver.create(GameConnectEvent.class));
            registry.events.add(new Identifier(ScriptItMod.MOD_NAME, "chat/incoming"), resolver.create(ChatIncomingEvent.class));
            registry.events.add(new Identifier(ScriptItMod.MOD_NAME, "chat/outgoing"), resolver.create(ChatOutgoingEvent.class));
            registry.events.add(new Identifier(ScriptItMod.MOD_NAME, "sound"), resolver.create(SoundEvent.class));
        } catch (DependencyException e) {
            e.printStackTrace();
        }
    }
}
