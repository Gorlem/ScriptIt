package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.dependencies.LanguageLoader;
import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.languages.LanguageInitializer;
import com.ddoerr.scriptit.api.languages.LanguageRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageLoaderImpl implements LanguageRegistry, Loadable, LanguageLoader {
    private Map<String, Language> languages = new HashMap<>();

    public void load() {
        List<LanguageInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints(new Identifier(ScriptItMod.MOD_NAME, "language").toString(), LanguageInitializer.class);

        for (LanguageInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    @Override
    public void registerLanguage(String name, Language language) {
        languages.put(name, language);
    }

    @Override
    public Collection<Language> getLanguages() {
        return languages.values();
    }

    @Override
    public Language findByName(String name) {
        return languages.getOrDefault(name, null);
    }
}
