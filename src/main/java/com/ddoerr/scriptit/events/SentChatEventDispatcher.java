package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.events.EventInitializer;
import com.ddoerr.scriptit.api.events.EventRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.callbacks.SendChatMessageCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class SentChatEventDispatcher implements EventInitializer, SendChatMessageCallback {
    Event event;

    @Override
    public void onInitialize(EventRegistry registry) {
        event = registry.registerEvent("sentChat");

        SendChatMessageCallback.EVENT.register(this);
    }

    @Override
    public TypedActionResult<String> onSendChatMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message);

        NamespaceRegistry namespace = event.createNamespace();
        namespace.registerVariable("message", (name, minecraft) -> chatMessage.getMessage());
        namespace.registerFunction("modify", (name, minecraft, arguments) -> {
            chatMessage.setMessage(arguments[0].toString());
            return null;
        });
        namespace.registerFunction("filter", (name, minecraft, arguments) -> {
            chatMessage.setActionResult(ActionResult.FAIL);
            return null;
        });
        event.dispatch();

        return chatMessage.toTypedResult();
    }

    static class ChatMessage {
        private String message;
        private ActionResult actionResult = ActionResult.PASS;

        public ChatMessage(String text) {
            this.message = text;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setActionResult(ActionResult actionResult) {
            this.actionResult = actionResult;
        }

        public TypedActionResult<String> toTypedResult() {
            return new TypedActionResult<>(actionResult, message);
        }
    }
}
