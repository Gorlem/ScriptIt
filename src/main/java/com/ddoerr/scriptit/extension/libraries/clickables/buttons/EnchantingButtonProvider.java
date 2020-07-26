package com.ddoerr.scriptit.extension.libraries.clickables.buttons;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.ducks.TooltipRenderedDuck;
import com.ddoerr.scriptit.mixin.ContainerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.EnchantingScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.container.EnchantingTableContainer;
import net.minecraft.util.Identifier;

public class EnchantingButtonProvider implements ButtonProvider {
    private MinecraftClient minecraft = MinecraftClient.getInstance();

    @Override
    public int getAmount(Screen screen) {
        return 3;
    }

    @Override
    public void click(Screen screen, int index) {
        EnchantingTableContainer container = ((EnchantingScreen) screen).getContainer();

        if (container.onButtonClick(minecraft.player, index)) {
            minecraft.interactionManager.clickButton(container.syncId, index);
        }
    }

    @Override
    public boolean matches(Screen screen) {
        return screen instanceof EnchantingScreen;
    }

    @Override
    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        for (int i = 0; i < 3; i++) {
            if (((ContainerAccessor)screen).invokeIsPointWithinBounds(60, 14 + 19 * i, 108, 17, mouseX, mouseY)) {
                if (((TooltipRenderedDuck) screen).wasTooltipRendered()) {
                    mouseY -= 18;
                }

                screen.renderTooltip(I18n.translate(new Identifier(ScriptItMod.MOD_NAME, "tooltip.button").toString(), i), mouseX, mouseY);
            }
        }
    }
}
