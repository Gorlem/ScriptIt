package com.ddoerr.scriptit.util.buttons;

import com.ddoerr.scriptit.mixin.ContainerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.EnchantingScreen;
import net.minecraft.container.EnchantingTableContainer;
import net.minecraft.enchantment.Enchantment;

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
                EnchantingTableContainer container = ((EnchantingScreen) screen).getContainer();

                int power = container.enchantmentPower[i];
                int level = container.enchantmentLevel[i];
                Enchantment enchantment = Enchantment.byRawId(container.enchantmentId[i]);

                if (power > 0 && level >= 0 && enchantment != null) {
                    mouseY -= 18;
                }

                screen.renderTooltip("Button " + i, mouseX, mouseY);
            }
        }
    }
}
