package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElementManager;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class HudElementManagerImpl implements Tickable, HudElementManager {
    private List<HudElementContainer> hudElements = new ArrayList<>();

    @Override
    public void add(HudElementContainer hudElement) {
        hudElements.add(hudElement);
    }

    @Override
    public List<HudElementContainer> getAll() {
        return hudElements;
    }

    @Override
    public void remove(HudElementContainer hudElement) {
        hudElements.remove(hudElement);
    }

    @Override
    public void renderAll() {
        for (HudElementContainer hudElement : getAll()) {
            hudElement.render(0, 0, 0);
        }
    }

    public void tick() {
        for (HudElementContainer hudElement : getAll()) {
            hudElement.tick();
        }
    }
}
