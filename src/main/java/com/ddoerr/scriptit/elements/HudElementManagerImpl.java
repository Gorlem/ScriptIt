package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.hud.HudElementContainer;
import com.ddoerr.scriptit.api.hud.HudElementManager;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.api.scripts.ScriptManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class HudElementManagerImpl implements Tickable, HudElementManager, Loadable {
    private List<HudElementContainer> hudElements = new ArrayList<>();

    private ScriptManager scriptManager;

    public HudElementManagerImpl(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public void add(HudElementContainer hudElement) {
        hudElements.add(hudElement);
        ScriptContainer scriptContainer = hudElement.getScriptContainer();
        scriptContainer.setCallback(message -> scriptManager.runScriptContainer(scriptContainer, message));
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

    @Override
    public void load() {
        HudRenderCallback.EVENT.register(delta -> renderAll());
    }
}
