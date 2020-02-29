package com.ddoerr.scriptit.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin({ChatHud.class})
public interface MessagesAccessor {
    @Accessor
    List<ChatHudLine> getMessages();
}
