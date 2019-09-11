package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElementInitializer;
import com.ddoerr.scriptit.api.hud.HudElementRegistry;

public class BuiltInHudElements implements HudElementInitializer {
    @Override
    public void onInitialize(HudElementRegistry registry) {
        registry.registerFactory(IconHudElement.class, IconHudElement::new);
        registry.registerFactory(TextHudElement.class, TextHudElement::new);
    }
}
