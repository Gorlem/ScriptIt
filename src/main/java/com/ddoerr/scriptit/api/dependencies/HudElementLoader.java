package com.ddoerr.scriptit.api.dependencies;

import com.ddoerr.scriptit.api.hud.HudElementProvider;

import java.util.Map;

public interface HudElementLoader {
    HudElementProvider findByName(String name);

    Map<String, HudElementProvider> getProviders();

    String getName(HudElementProvider provider);
}
