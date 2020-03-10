package com.ddoerr.scriptit.api.dependencies;

import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;

import java.util.Collection;

public interface LibraryLoader {
    Collection<NamespaceRegistry> getLibraries();
}
