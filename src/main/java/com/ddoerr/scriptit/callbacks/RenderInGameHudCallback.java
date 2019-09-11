package com.ddoerr.scriptit.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.Drawable;

public interface RenderInGameHudCallback extends Drawable {
    Event<RenderInGameHudCallback> EVENT = EventFactory.createArrayBacked(
            RenderInGameHudCallback.class,
            (listeners) -> (mouseX, mouseY, delta) -> {
                for (RenderInGameHudCallback event : listeners) {
                    event.render(mouseX, mouseY, delta);
                }
            }
    );
}
