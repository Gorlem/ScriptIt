package com.ddoerr.scriptit.util.slots;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.SlotActionType;
import spinnery.client.BaseScreen;
import spinnery.widget.WAbstractButton;
import spinnery.widget.WSlot;

import java.util.List;
import java.util.stream.Collectors;

public class SpinnerySlotProvider implements SlotProvider {
    @Override
    public void click(Screen screen, int index, int button, SlotActionType actionType) {
        List<WSlot> slots = getSlots(screen);

        if (index < 0 || index >= slots.size()) {
            return;
        }

        WSlot slot = slots.get(index);
        // TODO: Support the different SlotActionTypes
        slot.onMouseClicked(slot.getX() + 1, slot.getY() + 1, button);
    }

    @Override
    public int getAmount(Screen screen) {
        return getSlots(screen).size();
    }

    @Override
    public boolean matches(Screen screen) {
        return screen instanceof BaseScreen;
    }

    @Override
    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        List<WSlot> slots = getSlots(screen);

        for (int i = 0; i < slots.size(); i++) {
            WSlot slot = slots.get(i);

            if (slot.isWithinBounds(mouseX, mouseY)) {
                screen.renderTooltip("Slot " + i, mouseX, mouseY);
            }
        }
    }

    private List<WSlot> getSlots(Screen screen) {
        return ((BaseScreen)screen).getInterface().getAllWidgets()
                .stream()
                .filter(WSlot.class::isInstance)
                .map(WSlot.class::cast)
                .collect(Collectors.toList());
    }
}
