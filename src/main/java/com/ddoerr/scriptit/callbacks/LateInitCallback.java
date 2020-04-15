package com.ddoerr.scriptit.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public interface LateInitCallback {
    Event<LateInitCallback> EVENT = EventFactory.createArrayBacked(
            LateInitCallback.class,
            listeners -> minecraft -> {
                for (LateInitCallback event : listeners) {
                    event.onLateInitialize(minecraft);
                }
            }
    );

    void onLateInitialize(MinecraftClient minecraft);
}
