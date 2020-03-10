package com.ddoerr.scriptit.libraries.clickables.slots;

import com.ddoerr.scriptit.libraries.clickables.ClickablesProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.SlotActionType;
import net.minecraft.item.ItemStack;

public interface SlotProvider extends ClickablesProvider {
    void click(Screen screen, int index, int button, SlotActionType actionType);
    int findSlot(Screen screen, String id);
    ItemStack getSlotContent(Screen screen, int index);
}
