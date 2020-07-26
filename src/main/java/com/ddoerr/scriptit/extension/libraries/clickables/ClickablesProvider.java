package com.ddoerr.scriptit.extension.libraries.clickables;

import net.minecraft.client.gui.screen.Screen;

public interface ClickablesProvider {
    int getAmount(Screen screen);
    boolean matches(Screen screen);
    void renderTooltip(Screen screen, int mouseX, int mouseY);
}
