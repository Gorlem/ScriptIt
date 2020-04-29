package com.ddoerr.scriptit.api.hud;

import com.ddoerr.scriptit.elements.HudElementContainer;

import java.util.List;

public interface HudElementManager {
    void add(HudElementContainer hudElement);

    List<HudElementContainer> getAll();

    void remove(HudElementContainer hudElement);

    void renderAll();
}
