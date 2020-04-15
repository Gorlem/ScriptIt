package com.ddoerr.scriptit.mixin;

import com.ddoerr.scriptit.callbacks.SendChatMessageCallback;
import com.ddoerr.scriptit.ducks.TooltipRenderedDuck;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public abstract class MixinScreen implements TooltipRenderedDuck {
    public boolean tooltipRendered = false;

    @ModifyVariable(method = "sendMessage(Ljava/lang/String;Z)V", at = @At(value = "LOAD", ordinal = 1))
    private String modifyMessage(String message) {
        TypedActionResult<String> result = SendChatMessageCallback.EVENT.invoker().onSendChatMessage(message);

        if (result.getResult() == ActionResult.FAIL) {
            return null;
        }

        return result.getValue();
    }

    @Inject(method = "sendMessage(Ljava/lang/String;Z)V", at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.sendChatMessage(Ljava/lang/String;)V"), cancellable = true)
    private void onSendMessage(String message, boolean boolean_1, CallbackInfo info) {
        if (message == null) {
            info.cancel();
        }
    }

    @Inject(method = "renderTooltip(Ljava/util/List;II)V", at = @At("HEAD"))
    private void onRenderTooltip(List<String> text, int x, int y, CallbackInfo info) {
        tooltipRendered = true;
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(int mouseX, int mouseY, float delta, CallbackInfo info) {
        tooltipRendered = false;
    }

    @Override
    public boolean wasTooltipRendered() {
        return tooltipRendered;
    }
}
