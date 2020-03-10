package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.api.bus.EventBus;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.dependencies.Resolver;

public class EventImpl implements Event {
    String name;
    EventBus eventBus;

    public EventImpl(String name) {
        this.name = name;
        eventBus = Resolver.getInstance().resolve(EventBus.class);
    }

    @Override
    public void dispatch() {
        eventBus.publish(name, null);
    }

    @Override
    public void dispatch(NamespaceRegistry registry) {
        eventBus.publish(name, registry);
    }

    public String getName() {
        return name;
    }
}
