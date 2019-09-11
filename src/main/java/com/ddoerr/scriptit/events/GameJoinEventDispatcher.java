package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.events.EventInitializer;
import com.ddoerr.scriptit.api.events.EventRegistry;
import com.ddoerr.scriptit.callbacks.GameJoinCallback;

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
