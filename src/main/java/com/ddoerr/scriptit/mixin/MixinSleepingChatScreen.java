package com.ddoerr.scriptit.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SleepingChatScreen.class)
public class MixinSleepingChatScreen extends ChatScreen {
    public MixinSleepingChatScreen(String string_1) {
        super(string_1);
    }

    @Redirect(method = "keyPressed", at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.sendChatMessage(Ljava/lang/String;)V"))
    public void proxySendChatMessage(ClientPlayerEntity entity, String message) {
        this.sendMessage(message);
    }
}
