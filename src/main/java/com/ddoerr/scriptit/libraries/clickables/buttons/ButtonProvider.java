package com.ddoerr.scriptit.libraries.clickables.buttons;

import com.ddoerr.scriptit.libraries.clickables.ClickablesProvider;
import net.minecraft.client.gui.screen.Screen;

public interface ButtonProvider extends ClickablesProvider {
    void click(Screen screen, int index);
}
