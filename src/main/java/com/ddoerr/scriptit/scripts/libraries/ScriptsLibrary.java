package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.scripts.ThreadLifetimeManager;

public class ScriptsLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry namespace = registry.registerLibrary("scripts");
        namespace.registerFunction("stopall", (name, minecraft, arguments) -> Resolver.getInstance().resolve(ThreadLifetimeManager.class).stopAll());
    }
}
