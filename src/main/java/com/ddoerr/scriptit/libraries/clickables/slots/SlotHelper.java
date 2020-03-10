package com.ddoerr.scriptit.libraries.clickables.slots;

import com.ddoerr.scriptit.libraries.clickables.ClickablesHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.SlotActionType;
import net.minecraft.item.ItemStack;

public class SlotHelper extends ClickablesHelper<SlotProvider> {
    public SlotHelper() {
        provider.add(new SpinnerySlotProvider());
        provider.add(new CreativeSlotProvider());
        provider.add(new DefaultSlotProvider());
    }

    public void click(Screen screen, int index, int button, SlotActionType actionType) {
        getProvider(screen).ifPresent(p -> p.click(screen, index, button, actionType));
    }

    public int findSlot(Screen screen, String id) {
        return getProvider(screen).map(p -> p.findSlot(screen, id)).orElse(-1);
    }

    public ItemStack getSlotContent(Screen screen, int index) {
        return getProvider(screen).map(p -> p.getSlotContent(screen, index)).orElse(ItemStack.EMPTY);
    }
}
