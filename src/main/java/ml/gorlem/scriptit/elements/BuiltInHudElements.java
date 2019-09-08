package ml.gorlem.scriptit.elements;

import ml.gorlem.scriptit.api.hud.HudElementInitializer;
import ml.gorlem.scriptit.api.hud.HudElementRegistry;

public class BuiltInHudElements implements HudElementInitializer {
    @Override
    public void onInitialize(HudElementRegistry registry) {
        registry.registerFactory(IconHudElement.class, IconHudElement::new);
        registry.registerFactory(TextHudElement.class, TextHudElement::new);
    }
}
