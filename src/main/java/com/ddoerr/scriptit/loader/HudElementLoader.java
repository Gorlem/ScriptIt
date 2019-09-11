package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.api.hud.HudElementRegistry;
import com.ddoerr.scriptit.dependencies.Loadable;
import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementFactory;
import com.ddoerr.scriptit.api.hud.HudElementInitializer;
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
