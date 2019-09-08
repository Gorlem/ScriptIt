package ml.gorlem.scriptit.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface RenderHotbarCallback {
    Event<RenderHotbarCallback> SHOULD_RENDER = EventFactory.createArrayBacked(
            RenderHotbarCallback.class,
            (listeners) -> () -> {
                for (RenderHotbarCallback event : listeners) {
                    ActionResult result = event.shouldRender();

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    ActionResult shouldRender();
}
