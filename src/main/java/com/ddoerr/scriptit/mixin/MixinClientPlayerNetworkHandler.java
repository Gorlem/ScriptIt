package com.ddoerr.scriptit.mixin;

import com.ddoerr.scriptit.callbacks.ChatMessageCallback;
import com.ddoerr.scriptit.callbacks.GameJoinCallback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayerNetworkHandler implements ClientPlayPacketListener {

    @Inject(
            method = "onChatMessage",
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "net/minecraft/network/NetworkThreadUtils.forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V"))
    private void onChatMessageMixin(ChatMessageS2CPacket packet, CallbackInfo info) {
        TypedActionResult<Text> result = ChatMessageCallback.EVENT.invoker().onChatMessage(packet.getMessage());

        if (result.getResult() == ActionResult.FAIL) {
            info.cancel();
            return;
        }

        ((PacketTextAccessor)packet).setMessage(result.getValue());
    }

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onGameJoinMixin(GameJoinS2CPacket packet, CallbackInfo info) {
        GameJoinCallback.EVENT.invoker().onGameJoin();
    }
}
