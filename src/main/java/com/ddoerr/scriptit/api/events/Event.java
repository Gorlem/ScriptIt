package com.ddoerr.scriptit.api.events;

import com.ddoerr.scriptit.api.triggers.TriggerMessage;

import java.util.function.Consumer;

public interface Event {
    void registerListener(Consumer<TriggerMessage> messageConsumer);
}
