package com.ddoerr.scriptit.libraries.types.inventory;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.libraries.types.ItemModel;
import com.ddoerr.scriptit.mixin.ContainerAccessor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultInventoryModel extends InventoryModel {
    protected ContainerScreen<?> screen;
    protected PlayerInventory inventory;

    public DefaultInventoryModel(Screen screen, PlayerInventory inventory) {
        this.screen = (ContainerScreen<?>)screen;
        this.inventory = inventory;
    }

    private boolean isValidSlotIndex(int index) {
        return index < screen.getContainer().slots.size() && index >= 0;
    }

    private void clickSlot(int index, int button, SlotActionType actionType) {
        if (!isValidSlotIndex(index) && index != -999 ) {
            return;
        }

        Slot slot = index == -999 ? null : screen.getContainer().getSlot(index);
        ((ContainerAccessor)screen).invokeOnMouseClick(slot, index, button, actionType);
    }

    @Override
    public void renderTooltip(int mouseX, int mouseY) {
        Slot focusedSlot = ((ContainerAccessor)screen).getFocusedSlot();

        if (focusedSlot == null) {
            return;
        }

        if ((inventory.getCursorStack().isEmpty() && focusedSlot.hasStack())) {
            mouseY -= 18;
        }

        screen.renderTooltip(I18n.translate(new Identifier(ScriptItMod.MOD_NAME, "tooltip.slot").toString(), focusedSlot.id), mouseX, mouseY);
    }

    @Override
    public List<ItemModel> getSlots()  {
        return screen.getContainer().slots.stream().map(s -> ItemModel.From(s.getStack())).collect(Collectors.toList());
    }

    @Override
    public ItemModel slot(int slot) {
        if (!isValidSlotIndex(slot)) {
            return ItemModel.From(ItemStack.EMPTY);
        }

        return ItemModel.From(screen.getContainer().slots.get(slot).getStack());
    }

    @Override
    public int getSlotCount() {
        return screen.getContainer().slots.size();
    }

    @Override
    public ItemModel getActiveStack() {
        return ItemModel.From(inventory.getCursorStack());
    }

    @Override
    public void click(int slot, int button) {
        clickSlot(slot, button, SlotActionType.PICKUP);
    }

    @Override
    public void drop(int button) {
        clickSlot(-999, button, SlotActionType.PICKUP);
    }

    @Override
    public void drop(int slot, int button) {
        clickSlot(slot, button, SlotActionType.THROW);
    }

    @Override
    public void move(int slot) {
        clickSlot(slot, 0, SlotActionType.QUICK_MOVE);
    }

    @Override
    public void clone(int slot) {
        clickSlot(slot, 2, SlotActionType.CLONE);
    }

    @Override
    public void swap(int slot, int button) {
        clickSlot(slot, button, SlotActionType.SWAP);
    }

    @Override
    public void distribute(List<Integer> slots, int button) {
        int progress = 0;

        clickSlot(-999, Container.packClickData(progress, button), SlotActionType.QUICK_CRAFT);
        progress++;

        for (int id : slots) {
            clickSlot(id, Container.packClickData(progress, button), SlotActionType.QUICK_CRAFT);
        }

        clickSlot(-999, Container.packClickData(++progress, button), SlotActionType.QUICK_CRAFT);
    }
}
