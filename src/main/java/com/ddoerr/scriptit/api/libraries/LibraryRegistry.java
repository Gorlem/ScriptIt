package com.ddoerr.scriptit.api.libraries;

import net.minecraft.util.Tickable;

public interface LibraryRegistry {
    void registerTickable(Tickable tickable);
    void registerLibrary(String name, Model library);
}
