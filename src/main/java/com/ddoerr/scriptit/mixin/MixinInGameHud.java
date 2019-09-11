package com.ddoerr.scriptit.mixin;

import com.ddoerr.scriptit.callbacks.RenderHotbarCallback;
import com.ddoerr.scriptit.callbacks.RenderInGameHudCallback;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud extends DrawableHelper {
    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(float partialTicks, CallbackInfo info) {
        RenderInGameHudCallback.EVENT.invoker().render(0, 0, 0);
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void onRenderHotbar(float partialTicks, CallbackInfo info) {
        ActionResult shouldRender = RenderHotbarCallback.SHOULD_RENDER.invoker().shouldRender();

        if (shouldRender == ActionResult.FAIL) {
            info.cancel();
        }
    }
}
