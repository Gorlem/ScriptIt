package com.ddoerr.scriptit.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public interface ChatMessageCallback {
    Event<ChatMessageCallback> EVENT = EventFactory.createArrayBacked(
            ChatMessageCallback.class,
            (listeners) -> (text) -> {

                for (ChatMessageCallback event : listeners) {
                    TypedActionResult<Text> result = event.onChatMessage(text);

                    if (result.getResult() != ActionResult.PASS) {
                        return result;
                    }

                    text = result.getValue();
                }

                return new TypedActionResult<>(ActionResult.PASS, text);
            }
    );

    TypedActionResult<Text> onChatMessage(Text text);
}
