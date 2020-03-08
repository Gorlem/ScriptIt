package com.ddoerr.scriptit.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Mouse.class})
public interface ActiveMouseButtonAccessor {
    @Accessor
    int getActiveButton();
}
