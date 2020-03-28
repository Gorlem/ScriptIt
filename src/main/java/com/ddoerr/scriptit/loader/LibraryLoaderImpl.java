package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.dependencies.LibraryLoader;
import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.libraries.Library;
import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.Model;
import com.ddoerr.scriptit.loader.container.LibraryImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class LibraryLoaderImpl implements Tickable, Loadable, LibraryRegistry, LibraryLoader {
    private List<Tickable> tickables = new ArrayList<>();
    private List<Library> libraries = new ArrayList<>();

    public void load() {
        List<LibraryInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints(new Identifier(ScriptItMod.MOD_NAME, "library").toString(), LibraryInitializer.class);

        for (LibraryInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    @Override
    public List<Library> getLibraries() {
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
    public void registerLibrary(String name, Model model) {
        libraries.add(new LibraryImpl(name, model));
    }
}
