package com.ddoerr.scriptit.api.hud;

public interface HudElementRegistry {
    void registerHudElement(String name, HudElement provider);
}
