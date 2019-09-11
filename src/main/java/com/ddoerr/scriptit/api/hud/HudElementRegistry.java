package com.ddoerr.scriptit.api.hud;

public interface HudElementRegistry {
    void registerFactory(Class<? extends HudElement> hudElementClass, HudElementFactory factory);
}
