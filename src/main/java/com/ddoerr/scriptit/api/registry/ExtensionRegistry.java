package com.ddoerr.scriptit.api.registry;

import net.minecraft.util.Identifier;

public interface ExtensionRegistry {
    Extension registerExtension(Identifier identifier);
}
