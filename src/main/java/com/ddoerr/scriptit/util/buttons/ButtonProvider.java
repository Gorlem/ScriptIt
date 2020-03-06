package com.ddoerr.scriptit.util.buttons;

import com.ddoerr.scriptit.util.ClickablesProvider;
import net.minecraft.client.gui.screen.Screen;

public interface ButtonProvider extends ClickablesProvider {
    void click(Screen screen, int index);
}
