package com.ddoerr.scriptit.mixin;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.container.Slot;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({CreativeInventoryScreen.class})
public interface CreativeInventoryAccessor {
    @Accessor
    Slot getDeleteItemSlot();

    @Invoker
    void invokeSetSelectedTab(ItemGroup group);
}
