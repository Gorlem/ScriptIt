package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.EventBus;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.dependencies.Resolver;

public class EventTrigger implements Trigger {
    boolean shouldActivate = false;
    NamespaceRegistry registry = null;
    String name;
    EventBus eventBus;

    public EventTrigger(String name) {
        this.name = name;

        eventBus = Resolver.getInstance().resolve(EventBus.class);
        eventBus.subscribe(name, this::activate);
    }

    public void activate(Object registry) {
        shouldActivate = true;
        this.registry = (NamespaceRegistry) registry;
    }

    @Override
    public boolean canRun() {
        return shouldActivate;
    }

    @Override
    public void reset() {
        shouldActivate = false;
    }

    public String getName() {
        return name;
    }

    @Override
    public NamespaceRegistry additionalRegistry() {
        return registry;
    }

    @Override
    public void close() {
        eventBus.unsubscribe(name, this::activate);
    }
}
