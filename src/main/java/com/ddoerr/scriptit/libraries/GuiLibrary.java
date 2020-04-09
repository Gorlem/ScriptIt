package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.libraries.clickables.buttons.ButtonHelper;
import com.ddoerr.scriptit.libraries.types.inventory.InventoryModel;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public class GuiLibrary extends AnnotationBasedModel {
    private MinecraftClient minecraft;

    private ButtonHelper buttonHelper = new ButtonHelper();

    public GuiLibrary(MinecraftClient minecraft) {
        this.minecraft = minecraft;
    }

    @Getter
    public String getScreenName() {
        return Optional.ofNullable(minecraft.currentScreen).map(s -> s.getClass().getSimpleName()).orElse(StringUtils.EMPTY);
    }

    @Getter
    public InventoryModel getInventory() {
        if (minecraft.player == null) {
            return null;
        }

        return InventoryModel.From(minecraft.currentScreen, minecraft.player.inventory);
    }

    @Getter
    public int getButtonCount() {
        return buttonHelper.getAmount(minecraft.currentScreen);
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
}
