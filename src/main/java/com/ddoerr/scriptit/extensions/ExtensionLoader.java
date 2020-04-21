package com.ddoerr.scriptit.extensions;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.registry.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtensionLoader implements ExtensionRegistry {
    private Map<Identifier, Extension> extensions = new HashMap<>();

    private Resolver resolver;

    public ExtensionLoader(Resolver resolver) {
        this.resolver = resolver;
    }

    public void load() {
        List<ExtensionInitializer> entrypoints = FabricLoader.getInstance()
                .getEntrypoints(new Identifier(ScriptItMod.MOD_NAME, "extension").toString(), ExtensionInitializer.class);

        for (ExtensionInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    @Override
    public Extension registerExtension(Identifier identifier) {
        try {
            return extensions.put(identifier, resolver.create(ExtensionImpl.class));
        } catch (DependencyException e) {
            e.printStackTrace();
            return null;
        }
    }
}
