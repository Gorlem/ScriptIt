package com.ddoerr.scriptit.extensions;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.registry.Extension;
import com.ddoerr.scriptit.api.registry.ExtensionInitializer;
import com.ddoerr.scriptit.api.registry.ExtensionManager;
import com.ddoerr.scriptit.api.registry.ExtensionRegistry;
import com.ddoerr.scriptit.api.util.Named;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExtensionLoader implements ExtensionRegistry, Loadable, ExtensionManager {
    private Map<Identifier, ExtensionImpl> extensions = new HashMap<>();

    private Resolver resolver;

    public ExtensionLoader(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void load() {
        List<ExtensionInitializer> entrypoints = FabricLoader.getInstance()
                .getEntrypoints(new Identifier(ScriptItMod.MOD_NAME, "extensions").toString(), ExtensionInitializer.class);

        for (ExtensionInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    @Override
    public Extension registerExtension(Identifier identifier) {
        try {
            ExtensionImpl extension = resolver.create(ExtensionImpl.class);
            extensions.put(identifier, extension);
            return extension;
        } catch (DependencyException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T findByName(Class<T> registryType, String name) {
        return extensions.values()
                .stream()
                .map(extension -> extension.getRegistry(registryType))
                .map(registry -> registry.findByName(name))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public <T> List<Named<T>> getAll(Class<T> registryType) {
        return extensions.values()
                .stream()
                .map(extension -> extension.getRegistry(registryType))
                .flatMap(registry -> registry.getAll().stream())
                .collect(Collectors.toList());
    }
}
