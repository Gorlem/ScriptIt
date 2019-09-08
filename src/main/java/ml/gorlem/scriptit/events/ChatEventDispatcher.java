package ml.gorlem.scriptit.events;

import ml.gorlem.scriptit.api.events.Event;
import ml.gorlem.scriptit.api.events.EventInitializer;
import ml.gorlem.scriptit.api.events.EventRegistry;
import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import ml.gorlem.scriptit.callbacks.ChatMessageCallback;
import ml.gorlem.scriptit.loader.NamespaceRegistryContainer;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class ChatEventDispatcher implements EventInitializer, ChatMessageCallback {
    Event event;

    @Override
    public void onInitialize(EventRegistry registry) {
        event = registry.registerEvent("chat");

        ChatMessageCallback.EVENT.register(this);
    }

    @Override
    public TypedActionResult<Text> onChatMessage(Text text) {
        ChatMessage chatMessage = new ChatMessage(text);

        NamespaceRegistry event = new NamespaceRegistryContainer("event");
        event.registerVariable("json", (name, minecraft) -> BaseText.Serializer.toJson(chatMessage.getText()));
        event.registerVariable("message", (name, minecraft) -> chatMessage.getText().getString());
        event.registerFunction("modify", (name, minecraft, arguments) -> {
            try {
                chatMessage.setText(BaseText.Serializer.fromJson((String)arguments[0]));
            }
            catch (Exception e) {
                chatMessage.setText(new LiteralText(arguments[0].toString()));
            }

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
