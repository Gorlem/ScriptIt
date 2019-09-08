package ml.gorlem.scriptit.loader;

import ml.gorlem.scriptit.dependencies.Loadable;
import ml.gorlem.scriptit.api.hud.HudElement;
import ml.gorlem.scriptit.api.hud.HudElementFactory;
import ml.gorlem.scriptit.api.hud.HudElementInitializer;
import ml.gorlem.scriptit.api.hud.HudElementRegistry;
import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HudElementLoader implements HudElementRegistry, Loadable {
    Map<String, HudElementFactory> factories = new HashMap<>();

    public void load() {
        List<HudElementInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints("scriptit:hud", HudElementInitializer.class);

        for (HudElementInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    @Override
    public void registerFactory(Class<? extends HudElement> name, HudElementFactory factory) {
        factories.put(name.getSimpleName(), factory);
    }

    public HudElementFactory findByName(String name) {
        return factories.getOrDefault(name, null);
    }

    public Map<String, HudElementFactory> getFactories() {
        return factories;
    }
}
