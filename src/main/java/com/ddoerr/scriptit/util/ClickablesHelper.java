package com.ddoerr.scriptit.util;

import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;
import java.util.List;

public abstract class ClickablesHelper<T extends ClickablesProvider> {
    protected List<T> provider = new ArrayList<>();

    public int getAmount(Screen screen) {
        return provider.stream()
                .filter(p -> p.matches(screen))
                .findFirst()
                .map(p -> p.getAmount(screen))
                .orElse(0);
    }

    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        provider.stream()
                .filter(p -> p.matches(screen))
                .findFirst()
                .ifPresent(p -> p.renderTooltip(screen, mouseX, mouseY));
    }
}
