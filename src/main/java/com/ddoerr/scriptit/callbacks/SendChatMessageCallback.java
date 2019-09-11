package com.ddoerr.scriptit.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public interface SendChatMessageCallback {
    Event<SendChatMessageCallback> EVENT = EventFactory.createArrayBacked(
            SendChatMessageCallback.class,
            (listeners) -> (message) -> {

                for (SendChatMessageCallback event : listeners) {
                    TypedActionResult<String> result = event.onSendChatMessage(message);

                    if (result.getResult() != ActionResult.PASS) {
                        return result;
                    }

                    message = result.getValue();
                }

                return new TypedActionResult<>(ActionResult.PASS, message);
            }
    );

    TypedActionResult<String> onSendChatMessage(String message);
}
