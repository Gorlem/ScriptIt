package com.ddoerr.scriptit.extensions.minecraft;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.registry.ExtensionInitializer;
import com.ddoerr.scriptit.api.registry.ExtensionRegistry;
import com.ddoerr.scriptit.api.registry.RegistrableType;
import com.ddoerr.scriptit.api.registry.Registry;
import com.ddoerr.scriptit.libraries.PlayerLibrary;
import net.minecraft.util.Identifier;

public class MinecraftExtension implements ExtensionInitializer {
    @Override
    public void onInitialize(ExtensionRegistry registry) {
        Registry minecraft = registry.registerExtension(new Identifier(ScriptItMod.MOD_NAME, "minecraft"));
        minecraft.register(RegistrableType.Library, "player", PlayerLibrary.class);
    }
}
