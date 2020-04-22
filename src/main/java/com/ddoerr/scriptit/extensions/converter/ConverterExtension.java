package com.ddoerr.scriptit.extensions.converter;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.registry.ExtensionInitializer;
import com.ddoerr.scriptit.api.registry.ExtensionRegistry;
import com.ddoerr.scriptit.libraries.JsonLibrary;
import net.minecraft.util.Identifier;

public class ConverterExtension implements ExtensionInitializer {
    @Override
    public void onInitialize(ExtensionRegistry registry) {
        registry.registerExtension(new Identifier(ScriptItMod.MOD_NAME))
                .library("json", JsonLibrary.class);
    }
}
