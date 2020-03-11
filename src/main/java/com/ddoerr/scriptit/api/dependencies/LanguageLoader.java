package com.ddoerr.scriptit.api.dependencies;

import com.ddoerr.scriptit.api.languages.LanguageImplementation;

import java.util.Collection;

public interface LanguageLoader {
    Collection<LanguageImplementation> getLanguages();

    LanguageImplementation findByName(String name);
}
