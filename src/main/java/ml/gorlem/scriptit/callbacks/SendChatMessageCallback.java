package ml.gorlem.scriptit.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public interface SendChatMessageCallback {
    Event<SendChatMessageCallback> EVENT = EventFactory.createArrayBacked(
            SendChatMessageCallback.class,
            (listeners) -> (text) -> {

                for (SendChatMessageCallback event : listeners) {
                    TypedActionResult<Text> result = event.onSendChatMessage(text);

                    if (result.getResult() != ActionResult.PASS) {
                        return result;
                    }

                    text = result.getValue();
                }

                return new TypedActionResult<>(ActionResult.PASS, text);
            }
    );

    TypedActionResult<Text> onSendChatMessage(Text text);
}
