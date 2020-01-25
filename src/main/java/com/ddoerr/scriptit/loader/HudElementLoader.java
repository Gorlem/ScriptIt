package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.api.hud.*;
import com.ddoerr.scriptit.dependencies.Loadable;
import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HudElementLoader implements HudElementRegistry, Loadable {
    Map<String, HudElementProvider> providers = new HashMap<>();

    public void load() {
        List<HudElementInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints("scriptit:hud", HudElementInitializer.class);

        for (HudElementInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    @Override
    public void registerHudElement(String name, HudElementProvider provider) {
        providers.put(name, provider);
    }

    public HudElementProvider findByName(String name) {
        return providers.getOrDefault(name, null);
    }

    public Map<String, HudElementProvider> getProviders() {
        return providers;
    }

    public String getName(HudElementProvider provider) {
        return providers.entrySet().stream().filter(entry -> entry.getValue() == provider).findFirst().orElse(null).getKey();
    }
}
