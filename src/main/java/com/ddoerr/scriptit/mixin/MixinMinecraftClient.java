package com.ddoerr.scriptit.mixin;

import com.ddoerr.scriptit.callbacks.LateInitCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MinecraftClient.class})
public abstract class MixinMinecraftClient {
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onConstructed(RunArgs runArgs, CallbackInfo info) {
        LateInitCallback.EVENT.invoker().onLateInitialize((MinecraftClient)(Object)this);
    }
}
