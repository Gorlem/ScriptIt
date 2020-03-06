package com.ddoerr.scriptit.util.slots;

import com.ddoerr.scriptit.util.AbstractSpinneryClickablesProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.SlotActionType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import spinnery.client.BaseScreen;
import spinnery.common.BaseContainerScreen;
import spinnery.widget.WSlot;

import java.util.List;

public class SpinnerySlotProvider extends AbstractSpinneryClickablesProvider<WSlot> implements SlotProvider {
    public SpinnerySlotProvider() {
        super(WSlot.class);
    }

    @Override
    public boolean matches(Screen screen) {
        return screen instanceof BaseContainerScreen;
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

    @Override
    public int findSlot(Screen screen, String id) {
        return getWidgets((BaseScreen)screen).stream()
                .filter(s -> {
                    Identifier identifier = Registry.ITEM.getId(s.getStack().getItem());
                    return identifier.toString().equalsIgnoreCase(id) || identifier.getPath().equalsIgnoreCase(id);
                })
                .findFirst()
                .map(WSlot::getSlotNumber)
                .orElse(-1);
    }

    @Override
    public ItemStack getSlotContent(Screen screen, int index) {
        List<WSlot> slots = getWidgets((BaseScreen)screen);

        if (index < 0 || index >= slots.size()) {
            return ItemStack.EMPTY;
        }

        return slots.get(index).getStack();
    }
}
