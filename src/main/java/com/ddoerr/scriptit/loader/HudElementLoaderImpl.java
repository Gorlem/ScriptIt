package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.dependencies.HudElementLoader;
import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.hud.HudElementInitializer;
import com.ddoerr.scriptit.api.hud.HudElementProvider;
import com.ddoerr.scriptit.api.hud.HudElementRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HudElementLoaderImpl implements HudElementRegistry, Loadable, HudElementLoader {
    Map<String, HudElementProvider> providers = new HashMap<>();

    public void load() {
        List<HudElementInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints(new Identifier(ScriptItMod.MOD_NAME, "hud").toString(), HudElementInitializer.class);

        for (HudElementInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    @Override
    public void registerHudElement(String name, HudElementProvider provider) {
        providers.put(name, provider);
    }

    @Override
    public HudElementProvider findByName(String name) {
        return providers.getOrDefault(name, null);
    }

    @Override
    public Map<String, HudElementProvider> getProviders() {
        return providers;
    }

    @Override
    public String getName(HudElementProvider provider) {
        return providers.entrySet().stream().filter(entry -> entry.getValue() == provider).findFirst().orElse(null).getKey();
    }
}
