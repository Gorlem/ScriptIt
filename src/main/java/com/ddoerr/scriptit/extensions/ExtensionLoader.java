package com.ddoerr.scriptit.extensions;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.registry.ExtensionInitializer;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.util.List;

public class ExtensionLoader implements Loadable {
    private ScriptItRegistry registry;
    private Resolver resolver;

    public ExtensionLoader(ScriptItRegistry registry, Resolver resolver) {
        this.registry = registry;
        this.resolver = resolver;
    }

    @Override
    public void load() {
        List<ExtensionInitializer> entrypoints = FabricLoader.getInstance()
                .getEntrypoints(new Identifier(ScriptItMod.MOD_NAME, "extensions").toString(), ExtensionInitializer.class);

        for (ExtensionInitializer initializer : entrypoints) {
            initializer.onInitialize(registry, resolver);
        }
    }
}
