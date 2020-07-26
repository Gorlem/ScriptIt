package com.ddoerr.scriptit.api.events;

import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.triggers.TriggerMessageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AbstractEvent implements Event {
    protected List<Consumer<TriggerMessage>> listeners = new ArrayList<>();

    @Override
    public void registerListener(Consumer<TriggerMessage> messageConsumer) {
        listeners.add(messageConsumer);
    }

    @Override
    public void removeListener(Consumer<TriggerMessage> messageConsumer) {
        listeners.remove(messageConsumer);
    }

    protected void dispatch() {
        dispatch(new TriggerMessageImpl());
    }

    protected void dispatch(TriggerMessage message) {
        for (Consumer<TriggerMessage> listener : listeners) {
            listener.accept(message);
        }
    }
}
