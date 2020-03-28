package com.ddoerr.scriptit.api.scripts;

import com.ddoerr.scriptit.api.libraries.Library;

import java.util.Collection;

public interface Script {
    default boolean hasFileSource() {
        return getFileSource() != null;
    }
    default boolean hasStringSource() {
        return getStringSource() != null;
    }

    String getFileSource();
    String getStringSource();

    Collection<Library> getLibraries();

    String getName();
}
