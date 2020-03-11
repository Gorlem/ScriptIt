package com.ddoerr.scriptit.loader.container;

import com.ddoerr.scriptit.api.bus.EventBus;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.dependencies.Resolver;

public class EventImpl implements Event {
    String name;
    EventBus eventBus;
    NamespaceRegistry namespaceRegistry = null;

    public EventImpl(String name) {
        this.name = name;
        eventBus = Resolver.getInstance().resolve(EventBus.class);
    }

    @Override
    public void dispatch() {
        eventBus.publish(name, namespaceRegistry);
        namespaceRegistry = null;
    }

    @Override
    public NamespaceRegistry createNamespace() {
        return namespaceRegistry = new NamespaceRegistryImpl("event");
    }

    public String getName() {
        return name;
    }
}
