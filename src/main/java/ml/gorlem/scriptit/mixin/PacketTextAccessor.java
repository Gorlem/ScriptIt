package ml.gorlem.scriptit.mixin;

import net.minecraft.client.network.packet.ChatMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChatMessageS2CPacket.class)
public interface PacketTextAccessor {
    @Accessor
    void setMessage(Text message);
}
