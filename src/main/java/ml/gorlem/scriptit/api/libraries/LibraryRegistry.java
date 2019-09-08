package ml.gorlem.scriptit.api.libraries;

import net.minecraft.util.Tickable;

public interface LibraryRegistry {
    void registerTickable(Tickable tickable);
    NamespaceRegistry registerLibrary(String name);
}
