package ml.gorlem.scriptit.loader;

import ml.gorlem.scriptit.dependencies.Loadable;
import ml.gorlem.scriptit.api.libraries.LibraryInitializer;
import ml.gorlem.scriptit.api.libraries.LibraryRegistry;
import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LibraryLoader implements Tickable, Loadable, LibraryRegistry {
    private List<Tickable> tickables = new ArrayList<>();
    private List<NamespaceRegistry> libraries = new ArrayList<>();

    public void load() {
        List<LibraryInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints("scriptit:library", LibraryInitializer.class);

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
