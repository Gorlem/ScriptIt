package com.ddoerr.scriptit.extension.events;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.events.AbstractEvent;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.callbacks.OutgoingChatMessageCallback;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.triggers.TriggerMessageImpl;
import net.minecraft.util.TypedActionResult;

import java.time.Duration;

public class ChatOutgoingEvent extends AbstractEvent implements OutgoingChatMessageCallback {
    public ChatOutgoingEvent() {
        OutgoingChatMessageCallback.EVENT.register(this);
    }

    public TypedActionResult<String> onOutgoingChatMessage(String text) {
        MessageModel messageModel = new MessageModel(text);
        TriggerMessage message = new TriggerMessageImpl(messageModel, Duration.ofMillis(10));
        dispatch(message);
        return messageModel.getActionResult();
    }

    public static class MessageModel extends AnnotationBasedModel {
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
