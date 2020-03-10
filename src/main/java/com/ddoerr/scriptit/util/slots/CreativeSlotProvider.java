package com.ddoerr.scriptit.util.slots;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.mixin.ContainerAccessor;
import com.ddoerr.scriptit.mixin.CreativeInventoryAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

// TODO: Implement virtual inventory, for accessing search
public class CreativeSlotProvider implements SlotProvider {
    private MinecraftClient minecraft = MinecraftClient.getInstance();

    @Override
    public void click(Screen screen, int index, int button, SlotActionType actionType) {
        if (index >= getAmount(screen) || (index < 0 && index != -999)) {
            return;
        }

        ((CreativeInventoryAccessor)screen).invokeSetSelectedTab(ItemGroup.INVENTORY);

        Slot slot = index == -999 ? null : ((ContainerScreen<?>)screen).getContainer().getSlot(index);
        ((ContainerAccessor)screen).invokeOnMouseClick(slot, index, button, actionType);
    }

    @Override
    public int findSlot(Screen screen, String id) {
        ((CreativeInventoryAccessor)screen).invokeSetSelectedTab(ItemGroup.INVENTORY);

        return ((ContainerScreen<?>)screen).getContainer().slots
                .stream()
                .filter(s -> {
                    if (!s.hasStack()) {
                        return false;
                    }
                    Identifier identifier = Registry.ITEM.getId(s.getStack().getItem());
                    return identifier.toString().equalsIgnoreCase(id) || identifier.getPath().equalsIgnoreCase(id);
                })
                .findFirst()
                .map(s -> ((CreativeInventoryScreen)screen).getContainer().slots.indexOf(s))
                .orElse(-1);
    }

    @Override
    public ItemStack getSlotContent(Screen screen, int index) {
        if (index >= getAmount(screen) || index < 0) {
            return ItemStack.EMPTY;
        }

        ((CreativeInventoryAccessor)screen).invokeSetSelectedTab(ItemGroup.INVENTORY);
        return ((ContainerScreen<?>)screen).getContainer().getSlot(index).getStack();
    }

    @Override
    public int getAmount(Screen screen) {
        return 46;
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

        Slot deleteItemSlot = ((CreativeInventoryAccessor)screen).getDeleteItemSlot();
        if ((minecraft.player.inventory.getCursorStack().isEmpty() && focusedSlot.hasStack()) || focusedSlot == deleteItemSlot) {
            mouseY -= 18;
        }

        screen.renderTooltip(I18n.translate(new Identifier(ScriptItMod.MOD_NAME, "tooltip.slot").toString(), slotId), mouseX, mouseY);
    }
}
