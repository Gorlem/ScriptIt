package ml.gorlem.scriptit.loader;

import ml.gorlem.scriptit.dependencies.Loadable;
import ml.gorlem.scriptit.api.languages.LanguageImplementation;
import ml.gorlem.scriptit.api.languages.LanguageInitializer;
import ml.gorlem.scriptit.api.languages.LanguageRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Tickable;

import java.util.*;

public class LanguageLoader implements Tickable, LanguageRegistry, Loadable {
    private Map<String, LanguageImplementation> languages = new HashMap<>();

    public void load() {
        List<LanguageInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints("scriptit:language", LanguageInitializer.class);

        for (LanguageInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    @Override
    public void registerLanguage(String name, LanguageImplementation languageImplementation) {
        languages.put(name, languageImplementation);
    }

    public Collection<LanguageImplementation> getLanguages() {
        return languages.values();
    }

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
