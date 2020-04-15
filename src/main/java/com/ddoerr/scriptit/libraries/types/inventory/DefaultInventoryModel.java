package com.ddoerr.scriptit.libraries.types.inventory;

import com.ddoerr.scriptit.libraries.types.ItemModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultInventoryModel extends InventoryModel {
    PlayerInventory playerInventory;
    MinecraftClient minecraft;

    public DefaultInventoryModel(Screen screen, PlayerInventory playerInventory) {
        this.playerInventory = playerInventory;
        minecraft = MinecraftClient.getInstance();
    }

    @Override
    public void renderTooltip(int mouseX, int mouseY) {

    }

    @Override
    public List<ItemModel> getSlots() {
        return IntStream.range(0, PlayerInventory.getHotbarSize())
                .mapToObj(i -> playerInventory.getInvStack(i))
                .map(ItemModel::From)
                .collect(Collectors.toList());
    }

    @Override
    public ItemModel slot(int slot) {
        return ItemModel.From(playerInventory.getInvStack(slot));
    }

    @Override
    public int getSlotCount() {
        return PlayerInventory.getHotbarSize();
    }

    @Override
    public ItemModel getActiveStack() {
        return ItemModel.From(playerInventory.getMainHandStack());
    }

    @Override
    public void click(int slot, int button) {
        if (PlayerInventory.isValidHotbarIndex(slot)) {
            playerInventory.selectedSlot = slot;
        }
    }

    @Override
    public void drop(int button) {
        if (minecraft.player != null) {
            minecraft.player.dropSelectedItem(button == ClickType.WholeStack.button);
        }
    }

    @Override
    public void drop(int slot, int button) {
        click(slot);
        drop(button);
    }

    @Override
    public void move(int slot) {

    }

    @Override
    public void clone(int slot) {

    }

    @Override
    public void swap(int slot, int button) {

    }

    @Override
    public void distribute(List<Integer> slots, int button) {

    }
}
