package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.events.AbstractEvent;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.callbacks.OutgoingChatMessageCallback;
import net.minecraft.util.TypedActionResult;

public class ChatOutgoingEvent extends AbstractEvent implements OutgoingChatMessageCallback {
    public ChatOutgoingEvent() {
        OutgoingChatMessageCallback.EVENT.register(this);
    }

    public TypedActionResult<String> onOutgoingChatMessage(String message) {
        MessageModel messageModel = new MessageModel(message);
        dispatch(messageModel);
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
