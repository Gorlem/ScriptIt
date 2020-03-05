package com.ddoerr.scriptit.mixin;

import com.ddoerr.scriptit.callbacks.SendChatMessageCallback;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(Screen.class)
public abstract class MixinScreen {
    @Shadow
    List<Element> children;

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

    @Shadow
    abstract void renderTooltip(String text, int x, int y);

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(int mouseX, int mouseY, float delta, CallbackInfo info) {
        List<AbstractPressableButtonWidget> buttons = children
                .stream()
                .filter(AbstractPressableButtonWidget.class::isInstance)
                .map(AbstractPressableButtonWidget.class::cast)
                .collect(Collectors.toList());

        for (int i = 0; i < buttons.size(); i++) {
            AbstractPressableButtonWidget button = buttons.get(i);
            if (button.visible && button.isHovered()) {

                if ((Object) this instanceof BeaconScreen) {
                    mouseY -= 18;
                }
                renderTooltip("Button " + i, mouseX, mouseY);
            }
        }
    }
}
