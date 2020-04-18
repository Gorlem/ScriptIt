package com.ddoerr.scriptit.extensions.lua;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.registry.ExtensionInitializer;
import com.ddoerr.scriptit.api.registry.ExtensionRegistry;
import com.ddoerr.scriptit.api.registry.RegistrableType;
import com.ddoerr.scriptit.api.registry.Registry;
import com.ddoerr.scriptit.languages.lua.LuaLanguage;
import net.minecraft.util.Identifier;

public class LuaExtension implements ExtensionInitializer {
    @Override
    public void onInitialize(ExtensionRegistry registry) {
        Registry lua = registry.registerExtension(new Identifier(ScriptItMod.MOD_NAME, "lua"));
        lua.register(RegistrableType.Language, "lua", LuaLanguage.class);
    }
}
