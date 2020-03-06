package com.ddoerr.scriptit.util.slots;

import com.ddoerr.scriptit.mixin.ContainerAccessor;
import com.ddoerr.scriptit.mixin.DeleteItemSlotAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.item.ItemGroup;

public class CreativeSlotProvider implements SlotProvider {
    private MinecraftClient minecraft = MinecraftClient.getInstance();

    @Override
    public void click(Screen screen, int index, int button, SlotActionType actionType) {
        if (index >= getAmount(screen) || (index < 0 && index != -999)) {
            return;
        }

        Slot slot = index == -999 ? null : ((ContainerScreen<?>)screen).getContainer().getSlot(index);
        ((ContainerAccessor)screen).invokeOnMouseClick(slot, index, button, actionType);
    }

    @Override
    public int getAmount(Screen screen) {
        return -1;
    }

    @Override
    public boolean matches(Screen screen) {
        return screen instanceof CreativeInventoryScreen;
    }

    @Override
    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        Slot focusedSlot = ((ContainerAccessor)screen).getFocusedSlot();

        if (focusedSlot == null) {
            return;
        }

        int slotId = focusedSlot.id;
        if (((CreativeInventoryScreen)screen).getSelectedTab() == ItemGroup.INVENTORY.getIndex()) {
            slotId = ((CreativeInventoryScreen)screen).getContainer().slots.indexOf(focusedSlot);
        }

        Slot deleteItemSlot = ((DeleteItemSlotAccessor)screen).getDeleteItemSlot();
        if ((minecraft.player.inventory.getCursorStack().isEmpty() && focusedSlot.hasStack()) || focusedSlot == deleteItemSlot) {
            mouseY -= 18;
        }

        screen.renderTooltip("Slot " + slotId, mouseX, mouseY);
    }
}