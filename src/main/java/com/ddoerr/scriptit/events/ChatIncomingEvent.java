package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.events.AbstractEvent;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.callbacks.IncomingChatMessageCallback;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.triggers.TriggerMessageImpl;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;

import java.time.Duration;

public class ChatIncomingEvent extends AbstractEvent implements IncomingChatMessageCallback {
    public ChatIncomingEvent() {
        IncomingChatMessageCallback.EVENT.register(this);
    }

    public TypedActionResult<Text> onIncomingChatMessage(Text text) {
        MessageModel messageModel = new MessageModel(text);
        TriggerMessage message = new TriggerMessageImpl(messageModel, Duration.ofMillis(20));
        dispatch(message);
        return messageModel.getActionResult();
    }

    public static class MessageModel extends AnnotationBasedModel {
        private TypedActionResult<Text> actionResult;
        private Text text;

        public MessageModel(Text text) {
            actionResult = TypedActionResult.pass(text);
            this.text = text;
        }

        @Getter
        public String getJson() {
            return BaseText.Serializer.toJson(text);
        }

        @Getter
        public String getMessage() {
            return text.asFormattedString();
        }

        @Callable
        public void modify(String message) {
            Text newText;
            try {
                newText = BaseText.Serializer.fromJson(message);
            } catch (Exception e) {
                newText = new LiteralText(message);
            }

            actionResult = TypedActionResult.success(newText);
        }

        @Callable
        public void filter() {
            actionResult = TypedActionResult.fail(null);
        }

        public TypedActionResult<Text> getActionResult() {
            return actionResult;
        }
    }
}
