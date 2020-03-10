package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.scripts.ThreadLifetimeManager;

public class ScriptsLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry namespace = registry.registerLibrary("scripts");
        namespace.registerFunction("stop_all", (name, minecraft, arguments) -> Resolver.getInstance().resolve(ThreadLifetimeManager.class).stopAll());
    }
}
