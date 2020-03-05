package com.ddoerr.scriptit.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.container.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ContainerScreen.class})
public abstract class MixinContainerScreen extends Screen {
    protected MixinContainerScreen(Text title) {
        super(title);
    }

    @Shadow
    Slot focusedSlot;

    @Inject(method = "drawMouseoverTooltip", at = @At("HEAD"))
    private void drawMouseoverTooltipMixin(int mouseX, int mouseY, CallbackInfo info) {
        if (focusedSlot == null) {
            return;
        }

        if ((Object)this instanceof CreativeInventoryScreen) {
            Slot slot = ((DeleteItemSlotAccessor)this).getDeleteItemSlot();

            if (focusedSlot == slot) {
                mouseY -= 18;
            }
        }

        if ((minecraft.player.inventory.getCursorStack().isEmpty() && focusedSlot.hasStack())) {
            mouseY -= 18;
        }

        renderTooltip("Slot " + focusedSlot.id, mouseX, mouseY);
    }
}
