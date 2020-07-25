package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementFactory;

public class TextHudElementFactory implements HudElementFactory {
    private Resolver resolver;

    public TextHudElementFactory(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public HudElement createHudElement() {
        try {
            return resolver.create(TextHudElement.class);
        } catch (DependencyException e) {
            e.printStackTrace();
            return null;
        }
    }
}
