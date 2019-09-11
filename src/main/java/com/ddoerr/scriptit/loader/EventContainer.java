package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.events.EventManager;

public class EventContainer implements Event {
    String name;
    EventManager eventManager;

    public EventContainer(String name, EventManager eventManager) {
        this.name = name;
        this.eventManager = eventManager;
    }

    @Override
    public void dispatch() {
        eventManager.dispatch(this, null);
    }

    @Override
    public void dispatch(NamespaceRegistry registry) {
        eventManager.dispatch(this, registry);
    }

    public String getName() {
        return name;
    }
}
