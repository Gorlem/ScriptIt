package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.events.EventInitializer;
import com.ddoerr.scriptit.api.events.EventRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.callbacks.ChatMessageCallback;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class ReceivedChatEventDispatcher implements EventInitializer, ChatMessageCallback {
    Event event;

    @Override
    public void onInitialize(EventRegistry registry) {
        event = registry.registerEvent("receivedChat");

        ChatMessageCallback.EVENT.register(this);
    }

    @Override
    public TypedActionResult<Text> onChatMessage(Text text) {
        ChatMessage chatMessage = new ChatMessage(text);

        NamespaceRegistry namespace = event.createNamespace();
        namespace.registerVariable("json", (name, minecraft) -> BaseText.Serializer.toJson(chatMessage.getText()));
        namespace.registerVariable("message", (name, minecraft) -> chatMessage.getText().getString());
        namespace.registerFunction("modify", (name, minecraft, arguments) -> {
            try {
                chatMessage.setText(BaseText.Serializer.fromJson((String)arguments[0]));
            }
            catch (Exception e) {
                chatMessage.setText(new LiteralText(arguments[0].toString()));
            }

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
        private Text text;
        private ActionResult actionResult = ActionResult.PASS;

        public ChatMessage(Text text) {
            this.text = text;
        }

        public Text getText() {
            return text;
        }

        public void setText(Text text) {
            this.text = text;
        }

        public void setActionResult(ActionResult actionResult) {
            this.actionResult = actionResult;
        }

        public TypedActionResult<Text> toTypedResult() {
            return new TypedActionResult<>(actionResult, text);
        }
    }
}
