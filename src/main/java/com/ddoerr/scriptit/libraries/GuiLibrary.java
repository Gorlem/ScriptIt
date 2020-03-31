package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.libraries.clickables.buttons.ButtonHelper;
import com.ddoerr.scriptit.libraries.clickables.slots.SlotHelper;
import com.ddoerr.scriptit.libraries.types.InventoryModel;
import com.ddoerr.scriptit.libraries.types.ItemModel;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.container.SlotActionType;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public class GuiLibrary extends AnnotationBasedModel {
    private MinecraftClient minecraft;

    private ButtonHelper buttonHelper = new ButtonHelper();
    private SlotHelper slotHelper = new SlotHelper();

    public GuiLibrary(MinecraftClient minecraft) {
        this.minecraft = minecraft;
    }

    @Getter
    public String getScreen() {
        return Optional.ofNullable(minecraft.currentScreen).map(s -> s.getClass().getSimpleName()).orElse(StringUtils.EMPTY);
    }

    @Getter
    public InventoryModel getInventory() {
        if (minecraft.player == null || !(minecraft.currentScreen instanceof ContainerScreen<?>)) {
            return null;
        }

        return InventoryModel.From((ContainerScreen<?>)minecraft.currentScreen, minecraft.player.inventory);
    }

    @Getter
    public int getSlotCount() {
        return slotHelper.getAmount(minecraft.currentScreen);
    }

    @Getter
    public int getButtonCount() {
        return buttonHelper.getAmount(minecraft.currentScreen);
    }

    @Getter
    public ItemModel getActiveStack() {
        if (minecraft.currentScreen instanceof ContainerScreen && minecraft.player != null && minecraft.player.inventory.getCursorStack() != null) {
            return ItemModel.From(minecraft.player.inventory.getCursorStack());
        }
        return ItemModel.From(ItemStack.EMPTY);
    }

    @Callable
    public void clickSlot(List<Integer> ids, SlotActionType actionType, int button) {
        int progress = 0;

        if (actionType == SlotActionType.QUICK_CRAFT) {
            slotHelper.click(minecraft.currentScreen, -999, Container.packClickData(progress, button), actionType);
            button = Container.packClickData(++progress, button);
        }

        for (int id : ids) {
            slotHelper.click(minecraft.currentScreen, id, button, actionType);
        }

        if (actionType == SlotActionType.QUICK_CRAFT) {
            slotHelper.click(minecraft.currentScreen, -999, Container.packClickData(++progress, button), actionType);
        }
    }

    @Callable
    public void clickSlot(List<Integer> ids, SlotActionType actionType) {
        clickSlot(ids, actionType, 0);
    }

    @Callable
    public void clickSlot(List<Integer> ids) {
        clickSlot(ids, SlotActionType.PICKUP, 0);
    }

    @Callable
    public void clickSlot(int id, SlotActionType actionType, int button) {
        clickSlot(Lists.newArrayList(id), actionType, button);
    }

    @Callable
    public void clickSlot(int id, SlotActionType actionType) {
        clickSlot(Lists.newArrayList(id), actionType, 0);
    }

    @Callable
    public void clickSlot(int id) {
        clickSlot(Lists.newArrayList(id), SlotActionType.PICKUP, 0);
    }

    @Callable
    public void clickButton(List<Integer> ids) {
        for (int id : ids) {
            buttonHelper.click(minecraft.currentScreen, id);
        }
    }

    @Callable
    public void clickButton(int id) {
        clickButton(Lists.newArrayList(id));
    }

    @Callable
    public int findSlot(String id) {
        return slotHelper.findSlot(minecraft.currentScreen, id);
    }

    @Callable
    private ItemModel getSlotContent(int index) {
        return ItemModel.From(slotHelper.getSlotContent(minecraft.currentScreen, index));
    }
}
