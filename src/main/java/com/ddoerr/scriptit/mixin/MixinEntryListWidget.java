package com.ddoerr.scriptit.mixin;

import com.ddoerr.scriptit.callbacks.RenderEntryListBackgroundCallback;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntryListWidget.class)
public abstract class MixinEntryListWidget implements Drawable {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/render/Tessellator.draw()V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onRender(int mouseX, int mouseY, float delta, CallbackInfo info, int int_3, int int_4, Tessellator tessellator_1, BufferBuilder bufferBuilder_1) {
        ActionResult shouldRender = RenderEntryListBackgroundCallback.SHOULD_RENDER.invoker().shouldRender(this);

        if (shouldRender == ActionResult.FAIL) {
            bufferBuilder_1.clear();
        }
    }

    @Inject(
            method = "render",
            at = {
                    @At(value = "INVOKE", target = "net/minecraft/client/render/Tessellator.draw()V", ordinal = 1),
                    @At(value = "INVOKE", target = "net/minecraft/client/render/Tessellator.draw()V", ordinal = 2)
            },
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onRender(int mouseX, int mouseY, float delta, CallbackInfo info, int int_3, int int_4, Tessellator tessellator_1, BufferBuilder bufferBuilder_1, float float_2, int int_5, int int_6, int int_7) {
        ActionResult shouldRender = RenderEntryListBackgroundCallback.SHOULD_RENDER.invoker().shouldRender(this);

        if (shouldRender == ActionResult.FAIL) {
            bufferBuilder_1.clear();
        }
    }
}
