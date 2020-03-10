package com.ddoerr.scriptit.api.hud;

import com.ddoerr.scriptit.elements.HudElement;

import java.util.List;

public interface HudElementManager {
    void add(HudElement hudElement);

    List<HudElement> getAll();

    void remove(HudElement hudElement);

    void renderAll();
}
