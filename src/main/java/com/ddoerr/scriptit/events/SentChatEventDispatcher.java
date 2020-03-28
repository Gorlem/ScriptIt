package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.events.EventInitializer;
import com.ddoerr.scriptit.api.events.EventRegistry;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.callbacks.SendChatMessageCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class SentChatEventDispatcher implements EventInitializer {
    Event event;

    @Override
    public void onInitialize(EventRegistry registry) {
        event = registry.registerEvent("sentChat");

        SendChatMessageCallback.EVENT.register(this::onSendChatMessage);
    }

    public TypedActionResult<String> onSendChatMessage(String message) {
        MessageModel messageModel = new MessageModel(message);
        event.dispatch(messageModel);
        return messageModel.getActionResult();
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

    static class MessageModel extends AnnotationBasedModel {
        private TypedActionResult<String> actionResult;
        private String message;

        public MessageModel(String message) {
            actionResult = TypedActionResult.pass(message);
            this.message = message;
        }

        @Getter
        public String getMessage() {
            return message;
        }

        @Callable
        public void modify(String message) {
            actionResult = TypedActionResult.success(message);
        }

        @Callable
        public void filter() {
            actionResult = TypedActionResult.fail(null);
        }

        public TypedActionResult<String> getActionResult() {
            return actionResult;
        }
    }
}
