package com.ddoerr.scriptit.util.slots;

import com.ddoerr.scriptit.util.AbstractSpinneryClickablesProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.SlotActionType;
import spinnery.client.BaseScreen;
import spinnery.widget.WSlot;

import java.util.List;

public class SpinnerySlotProvider extends AbstractSpinneryClickablesProvider<WSlot> implements SlotProvider {
    public SpinnerySlotProvider() {
        super(WSlot.class);
    }

    @Override
    public void click(Screen screen, int index, int button, SlotActionType actionType) {
        List<WSlot> slots = getWidgets((BaseScreen)screen);

        if (index < 0 || index >= slots.size()) {
            return;
        }

        WSlot slot = slots.get(index);
        // TODO: Support the different SlotActionTypes
        slot.onMouseClicked(slot.getX() + 1, slot.getY() + 1, button);
    }
}
