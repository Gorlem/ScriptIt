package com.ddoerr.scriptit.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public interface IncomingChatMessageCallback {
    Event<IncomingChatMessageCallback> EVENT = EventFactory.createArrayBacked(
            IncomingChatMessageCallback.class,
            (listeners) -> (text) -> {

                for (IncomingChatMessageCallback event : listeners) {
                    TypedActionResult<Text> result = event.onIncomingChatMessage(text);

                    if (result.getResult() != ActionResult.PASS) {
                        return result;
                    }

                    text = result.getValue();
                }

                return new TypedActionResult<>(ActionResult.PASS, text);
            }
    );

    TypedActionResult<Text> onIncomingChatMessage(Text text);
}
