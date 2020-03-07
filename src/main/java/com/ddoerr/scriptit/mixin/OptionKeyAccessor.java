package com.ddoerr.scriptit.mixin;

import net.minecraft.client.options.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Option.class})
public interface OptionKeyAccessor {
    @Accessor
    String getKey();
}
