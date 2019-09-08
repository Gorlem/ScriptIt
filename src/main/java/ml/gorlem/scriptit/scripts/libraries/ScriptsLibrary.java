package ml.gorlem.scriptit.scripts.libraries;

import ml.gorlem.scriptit.ScriptItMod;
import ml.gorlem.scriptit.api.libraries.LibraryRegistry;
import ml.gorlem.scriptit.api.libraries.LibraryInitializer;
import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.scripts.ThreadLifetimeManager;

public class ScriptsLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry namespace = registry.registerLibrary("scripts");
        namespace.registerFunction("stopall", (name, minecraft, arguments) -> Resolver.getInstance().resolve(ThreadLifetimeManager.class).stopAll());
    }
}
