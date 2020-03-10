package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LibraryLoader implements Tickable, Loadable, LibraryRegistry {
    private List<Tickable> tickables = new ArrayList<>();
    private List<NamespaceRegistry> libraries = new ArrayList<>();

    public void load() {
        List<LibraryInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints(new Identifier(ScriptItMod.MOD_NAME, "library").toString(), LibraryInitializer.class);

        for (LibraryInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    public Collection<NamespaceRegistry> getLibraries() {
        return libraries;
    }

    @Override
    public void tick() {
        for (Tickable tickable : tickables) {
            tickable.tick();
        }
    }

    @Override
    public void registerTickable(Tickable tickable) {
        tickables.add(tickable);
    }

    @Override
    public NamespaceRegistry registerLibrary(String name) {
        NamespaceRegistry registry = new NamespaceRegistryContainer(name);
        libraries.add(registry);
        return registry;
    }
}
