package com.ddoerr.scriptit.languages;

import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.libraries.Model;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import net.minecraft.util.Identifier;

public class LanguageManagerImpl implements Loadable {
    private ScriptItRegistry registry;

    public LanguageManagerImpl(ScriptItRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void load() {
        for (Language language : registry.languages) {
            for (Model library : registry.libraries) {
                Identifier identifier = registry.libraries.getId(library);
                language.loadLibrary(identifier.toString(), library);
            }
        }
    }
}
