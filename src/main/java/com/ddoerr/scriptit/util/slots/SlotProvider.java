package com.ddoerr.scriptit.util.slots;

import com.ddoerr.scriptit.util.ClickablesProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.SlotActionType;

public interface SlotProvider extends ClickablesProvider {
    void click(Screen screen, int index, int button, SlotActionType actionType);
}
