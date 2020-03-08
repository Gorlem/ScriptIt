package com.ddoerr.scriptit.mixin;

import net.minecraft.client.options.DoubleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({DoubleOption.class})
public interface StepAccessor {
    @Accessor
    float getStep();
}
