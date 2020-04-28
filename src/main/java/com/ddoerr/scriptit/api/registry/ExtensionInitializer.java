package com.ddoerr.scriptit.api.registry;

import com.ddoerr.scriptit.api.dependencies.Resolver;

public interface ExtensionInitializer {
    void onInitialize(ScriptItRegistry registry, Resolver resolver);
}
