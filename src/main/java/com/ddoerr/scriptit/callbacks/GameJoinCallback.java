package com.ddoerr.scriptit.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface GameJoinCallback {
    Event<GameJoinCallback> EVENT = EventFactory.createArrayBacked(
            GameJoinCallback.class,
            (listeners) -> () -> {
                for (GameJoinCallback event : listeners) {
                    event.onGameJoin();
                }
            }
    );

    void onGameJoin();
}
