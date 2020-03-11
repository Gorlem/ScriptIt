package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.dependencies.LanguageLoader;
import com.ddoerr.scriptit.api.languages.LanguageRegistry;
import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.languages.LanguageImplementation;
import com.ddoerr.scriptit.api.languages.LanguageInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;

import java.util.*;

public class LanguageLoaderImpl implements Tickable, LanguageRegistry, Loadable, LanguageLoader {
    private Map<String, LanguageImplementation> languages = new HashMap<>();

    public void load() {
        List<LanguageInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints(new Identifier(ScriptItMod.MOD_NAME, "language").toString(), LanguageInitializer.class);

        for (LanguageInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    @Override
    public void registerLanguage(String name, LanguageImplementation languageImplementation) {
        languages.put(name, languageImplementation);
    }

    @Override
    public Collection<LanguageImplementation> getLanguages() {
        return languages.values();
    }

    @Override
    public LanguageImplementation findByName(String name) {
        return languages.getOrDefault(name, null);
    }

    @Override
    public void tick() {
        for (LanguageImplementation language : languages.values()) {
            language.tick();
        }
    }
}
