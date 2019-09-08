package ml.gorlem.scriptit.events;

import ml.gorlem.scriptit.api.events.Event;
import ml.gorlem.scriptit.api.events.EventInitializer;
import ml.gorlem.scriptit.api.events.EventRegistry;
import ml.gorlem.scriptit.callbacks.GameJoinCallback;

public class GameJoinEventDispatcher implements EventInitializer, GameJoinCallback {
    private Event event;

    @Override
    public void onInitialize(EventRegistry registry) {
        event = registry.registerEvent("gameJoin");

        GameJoinCallback.EVENT.register(this);
    }

    @Override
    public void onGameJoin() {
        event.dispatch();
    }
}
