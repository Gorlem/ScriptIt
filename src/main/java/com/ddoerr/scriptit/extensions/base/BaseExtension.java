package com.ddoerr.scriptit.extensions.base;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.registry.ExtensionInitializer;
import com.ddoerr.scriptit.api.registry.ExtensionRegistry;
import com.ddoerr.scriptit.libraries.ScriptsLibrary;
import net.minecraft.util.Identifier;

public class BaseExtension implements ExtensionInitializer {
    @Override
    public void onInitialize(ExtensionRegistry registry) {
        registry.registerExtension(new Identifier(ScriptItMod.MOD_NAME, "base"))
                .library("scripts", ScriptsLibrary.class);
    }
}
