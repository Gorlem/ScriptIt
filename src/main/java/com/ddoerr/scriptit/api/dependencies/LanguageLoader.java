package com.ddoerr.scriptit.api.dependencies;

import com.ddoerr.scriptit.api.languages.Language;

import java.util.Collection;

public interface LanguageLoader {
    Collection<Language> getLanguages();

    Language findByName(String name);
}
