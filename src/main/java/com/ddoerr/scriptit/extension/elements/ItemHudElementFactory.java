package com.ddoerr.scriptit.extension.elements;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementFactory;

public class ItemHudElementFactory implements HudElementFactory {
    private Resolver resolver;

    public ItemHudElementFactory(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public HudElement createHudElement() {
        try {
            return resolver.create(ItemHudElement.class);
        } catch (DependencyException e) {
            e.printStackTrace();
            return null;
        }
    }
}
