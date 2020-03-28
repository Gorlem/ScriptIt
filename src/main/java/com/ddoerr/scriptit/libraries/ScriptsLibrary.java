package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.api.scripts.ThreadLifetimeManager;

public class ScriptsLibrary extends AnnotationBasedModel {
    ThreadLifetimeManager lifetimeManager;

    public ScriptsLibrary(ThreadLifetimeManager lifetimeManager) {
        this.lifetimeManager = lifetimeManager;
    }

    @Callable
    public int stopAll() {
        return lifetimeManager.stopAll();
    }
}
