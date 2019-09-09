package ml.gorlem.scriptit.mixin;

import ml.gorlem.scriptit.callbacks.ChatMessageCallback;
import ml.gorlem.scriptit.callbacks.GameJoinCallback;
import ml.gorlem.scriptit.callbacks.SendChatMessageCallback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.ChatMessageS2CPacket;
import net.minecraft.client.network.packet.GameJoinS2CPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.server.network.packet.ChatMessageC2SPacket;
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
                    target = "net/minecraft/network/NetworkThreadUtils.forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/ThreadExecutor;)V"))
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

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo info) {
        if (packet.getClass().equals(ChatMessageC2SPacket.class)) {
            ChatMessageC2SPacket chatPacket = (ChatMessageC2SPacket)packet;

            TypedActionResult<String> result = SendChatMessageCallback.EVENT.invoker().onSendChatMessage(chatPacket.getChatMessage());

            if (result.getResult() == ActionResult.FAIL) {
                info.cancel();
                return;
            }

            ((SendPacketTextAccessor)packet).setChatMessage(result.getValue());
        }
    }
}
