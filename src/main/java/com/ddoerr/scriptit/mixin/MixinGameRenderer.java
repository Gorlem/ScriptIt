package com.ddoerr.scriptit.mixin;

import com.ddoerr.scriptit.extension.libraries.clickables.buttons.ButtonHelper;
import com.ddoerr.scriptit.models.inventory.InventoryModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameRenderer.class})
public abstract class MixinGameRenderer {
    private ButtonHelper buttonHelper = new ButtonHelper();

    @Shadow
    private MinecraftClient client;

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(float tickDelta, long startTime, boolean tick, CallbackInfo info) {
        int mouseX = (int)(client.mouse.getX() * (double)client.getWindow().getScaledWidth() / (double)client.getWindow().getWidth());
        int mouseY = (int)(client.mouse.getY() * (double)client.getWindow().getScaledHeight() / (double)client.getWindow().getHeight());

        buttonHelper.renderTooltip(client.currentScreen, mouseX, mouseY);
        if (client.player != null) {
            InventoryModel.From(client.currentScreen, client.player.inventory).renderTooltip(mouseX, mouseY);
        }
    }
}
