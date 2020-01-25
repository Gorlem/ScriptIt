package com.ddoerr.scriptit.elements;

import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class HudElementManager implements Tickable {
    private List<HudElement> hudElements = new ArrayList<HudElement>();

    public void add(HudElement hudElement) {
        hudElements.add(hudElement);
    }

    public List<HudElement> getAll() {
        return hudElements;
    }

    public void remove(HudElement hudElement) {
        hudElements.remove(hudElement);
    }

    public void renderAll(int mouseX, int mouseY, float v) {
        for (HudElement hudElement : getAll()) {
            hudElement.render(0, 0, 0);
        }
    }

    public void tick() {
        for (HudElement hudElement : getAll()) {
            if (hudElement instanceof Tickable) {
                ((Tickable) hudElement).tick();
            }
        }
    }
}
