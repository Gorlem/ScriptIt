package com.ddoerr.scriptit.extension.libraries.clickables;

import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ClickablesHelper<T extends ClickablesProvider> {
    protected List<T> provider = new ArrayList<>();

    protected Optional<T> getProvider(Screen screen) {
        return provider.stream().filter(p -> p.matches(screen)).findFirst();
    }

    public int getAmount(Screen screen) {
        return getProvider(screen).map(p -> p.getAmount(screen)).orElse(0);
    }

    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        getProvider(screen).ifPresent(p -> p.renderTooltip(screen, mouseX, mouseY));
    }
}
