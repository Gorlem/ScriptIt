package com.ddoerr.scriptit.extensions.minecraft;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.registry.*;
import com.ddoerr.scriptit.libraries.*;
import net.minecraft.util.Identifier;

public class MinecraftExtension implements ExtensionInitializer {
    @Override
    public void onInitialize(ExtensionRegistry registry) {
         registry.registerExtension(new Identifier(ScriptItMod.MOD_NAME, "minecraft"))
                .library("player", PlayerLibrary.class)
                 .library("chat", ChatLibrary.class)
                 .library("game", GameLibrary.class)
                 .library("gui", GuiLibrary.class)
                 .library("keyboard", KeyboardLibrary.class)
                 .library("options", OptionsLibrary.class)
                 .library("scoreboard", ScoreboardLibrary.class)
                 .library("server", ServerLibrary.class);
    }
}
