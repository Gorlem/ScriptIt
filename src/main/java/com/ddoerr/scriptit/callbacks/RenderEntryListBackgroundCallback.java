package com.ddoerr.scriptit.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.Drawable;
import net.minecraft.util.ActionResult;

public interface RenderEntryListBackgroundCallback {
    Event<RenderEntryListBackgroundCallback> SHOULD_RENDER = EventFactory.createArrayBacked(
            RenderEntryListBackgroundCallback.class,
            (listeners) -> (widget) -> {
                for (RenderEntryListBackgroundCallback event : listeners) {
                    ActionResult result = event.shouldRender(widget);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    ActionResult shouldRender(Drawable widget);
}
