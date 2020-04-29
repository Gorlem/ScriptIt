package com.ddoerr.scriptit.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public interface OutgoingChatMessageCallback {
    Event<OutgoingChatMessageCallback> EVENT = EventFactory.createArrayBacked(
            OutgoingChatMessageCallback.class,
            (listeners) -> (message) -> {

                for (OutgoingChatMessageCallback event : listeners) {
                    TypedActionResult<String> result = event.onOutgoingChatMessage(message);

                    if (result.getResult() != ActionResult.PASS) {
                        return result;
                    }

                    message = result.getValue();
                }

                return new TypedActionResult<>(ActionResult.PASS, message);
            }
    );

    TypedActionResult<String> onOutgoingChatMessage(String message);
}
