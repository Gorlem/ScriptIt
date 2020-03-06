package com.ddoerr.scriptit.util.slots;

import com.ddoerr.scriptit.util.ClickablesHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.SlotActionType;

public class SlotHelper extends ClickablesHelper<SlotProvider> {
    public SlotHelper() {
        provider.add(new SpinnerySlotProvider());
        provider.add(new CreativeSlotProvider());
        provider.add(new DefaultSlotProvider());
    }

    public void click(Screen screen, int index, int button, SlotActionType actionType) {
        provider.stream()
                .filter(p -> p.matches(screen))
                .findFirst()
                .ifPresent(p -> p.click(screen, index, button, actionType));
    }
}
