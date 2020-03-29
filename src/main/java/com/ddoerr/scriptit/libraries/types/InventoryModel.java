package com.ddoerr.scriptit.libraries.types;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.libraries.clickables.slots.SlotHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.Container;
import net.minecraft.container.SlotActionType;
import net.minecraft.inventory.Inventory;

import java.util.List;

public class InventoryModel extends AnnotationBasedModel {
    public static InventoryModel From(Screen screen, Inventory inventory) {
        InventoryModel inventoryModel = new InventoryModel();
        inventoryModel.screen = screen;
        inventoryModel.inventory = inventory;
        return inventoryModel;
    }

    private Screen screen;
    private Inventory inventory;

    private SlotHelper slotHelper = new SlotHelper();

    @Getter
    public List<ItemModel> getSlots() {
        return null;
    }

    @Getter
    public ItemModel getActiveStack() {
        return null;
    }

    @Callable
    public void click(int slot, ClickType clickType) {
        click(slot, clickType.button);
    }

    @Callable
    public void click(int slot, int button) {
        slotHelper.click(screen, slot, button, SlotActionType.PICKUP);
    }

    @Callable
    public void drop(ClickType clickType) {
        drop(clickType.button);
    }

    @Callable
    public void drop(int button) {
        slotHelper.click(screen, -999, button, SlotActionType.PICKUP);
    }

    @Callable
    public void drop(int slot, ClickType clickType) {
        drop(slot, clickType.button == 0 ? 1 : 0);
    }

    @Callable
    public void drop(int slot, int button) {
        slotHelper.click(screen, slot, button, SlotActionType.THROW);
    }

    @Callable
    public void move(int slot) {
        slotHelper.click(screen, slot, 0, SlotActionType.QUICK_MOVE);
    }

    @Callable
    public void clone(int slot) {
        slotHelper.click(screen, slot, 2, SlotActionType.CLONE);
    }

    @Callable
    public void swap(int slot, int button) {
        slotHelper.click(screen, slot, button, SlotActionType.SWAP);
    }

    @Callable
    public void distribute(List<Integer> slots, ClickType clickType) {
        distribute(slots, clickType.button);
    }

    @Callable
    public void distribute(List<Integer> slots, int button) {
        int progress = 0;

        slotHelper.click(screen, -999, Container.packClickData(progress, button), SlotActionType.QUICK_CRAFT);
        progress++;

        for (int id : slots) {
            slotHelper.click(screen, id, Container.packClickData(progress, button), SlotActionType.QUICK_CRAFT);
        }

        slotHelper.click(screen, -999, Container.packClickData(++progress, button), SlotActionType.QUICK_CRAFT);
    }

    public enum ClickType {
        WholeStack(0),
        Evenly(0),
        Left(0),
        HalfStack(1),
        SingleItem(1),
        Right(1);

        private final int button;
        ClickType(int button) {
            this.button = button;
        }
    }
}
