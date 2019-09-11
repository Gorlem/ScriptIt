package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.events.EventInitializer;
import com.ddoerr.scriptit.api.events.EventRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.callbacks.SendChatMessageCallback;
import com.ddoerr.scriptit.loader.NamespaceRegistryContainer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class SendChatEventDispatcher implements EventInitializer, SendChatMessageCallback {
    Event event;

    @Override
    public void onInitialize(EventRegistry registry) {
        event = registry.registerEvent("sendChat");

        SendChatMessageCallback.EVENT.register(this);
    }

    @Override
    public TypedActionResult<String> onSendChatMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message);

        NamespaceRegistry event = new NamespaceRegistryContainer("event");
        event.registerVariable("message", (name, minecraft) -> chatMessage.getMessage());
        event.registerFunction("modify", (name, minecraft, arguments) -> {
            chatMessage.setMessage(arguments[0].toString());
            return null;
        });
        event.registerFunction("filter", (name, minecraft, arguments) -> {
            chatMessage.setActionResult(ActionResult.FAIL);
            return null;
        });
        this.event.dispatch(event);

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
