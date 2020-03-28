package com.ddoerr.scriptit.api.dependencies;

import com.ddoerr.scriptit.api.libraries.Library;

import java.util.List;

public interface LibraryLoader {
    List<Library> getLibraries();
}
