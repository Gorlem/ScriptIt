package com.ddoerr.scriptit.mixin;

import com.ddoerr.scriptit.callbacks.SendChatMessageCallback;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen {
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
}
