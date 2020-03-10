package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElementManager;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class HudElementManagerImpl implements Tickable, HudElementManager {
    private List<HudElement> hudElements = new ArrayList<>();

    @Override
    public void add(HudElement hudElement) {
        hudElements.add(hudElement);
    }

    @Override
    public List<HudElement> getAll() {
        return hudElements;
    }

    @Override
    public void remove(HudElement hudElement) {
        hudElements.remove(hudElement);
    }

    @Override
    public void renderAll() {
        for (HudElement hudElement : getAll()) {
            hudElement.render(0, 0, 0);
        }
    }

    public void tick() {
        for (HudElement hudElement : getAll()) {
            hudElement.tick();
        }
    }
}
