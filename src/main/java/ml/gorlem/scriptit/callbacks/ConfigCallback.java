package ml.gorlem.scriptit.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ConfigCallback {
    Event<ConfigCallback> EVENT = EventFactory.createArrayBacked(
            ConfigCallback.class,
            (listeners) -> (Class<?> source) -> {
                for (ConfigCallback event : listeners) {
                    event.saveConfig(source);
                }
            }
    );

    void saveConfig(Class<?> source);
}
