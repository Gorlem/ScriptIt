package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElementInitializer;
import com.ddoerr.scriptit.api.hud.HudElementRegistry;

public class DefaultHudElements implements HudElementInitializer {
    @Override
    public void onInitialize(HudElementRegistry registry) {
        registry.registerHudElement("Icon", new IconHudElement());
        registry.registerHudElement("Text", new TextHudElement());
    }
}
