package com.ddoerr.scriptit.mixin;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.container.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({CreativeInventoryScreen.class})
public interface DeleteItemSlotAccessor {
    @Accessor
    Slot getDeleteItemSlot();
}
