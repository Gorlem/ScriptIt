package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.api.events.AbstractEvent;
import com.ddoerr.scriptit.callbacks.GameJoinCallback;

public class GameConnectEvent extends AbstractEvent implements GameJoinCallback {
    public GameConnectEvent() {
        GameJoinCallback.EVENT.register(this);
    }

    @Override
    public void onGameJoin() {
        dispatch();
    }
}
