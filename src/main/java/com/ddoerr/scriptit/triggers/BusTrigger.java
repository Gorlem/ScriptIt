package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.Bus;
import com.ddoerr.scriptit.EventBus;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.dependencies.Resolver;

import java.util.Collection;

public class BusTrigger implements Trigger {
    boolean shouldActivate = false;
    NamespaceRegistry registry = null;
    String id;
    Bus<Object> bus;

    public BusTrigger(String id) {
        this.id = id;

        bus = Resolver.getInstance().resolve(EventBus.class);

        bus.subscribe(this.id, this::activate);
    }

    public void activate(Object registry) {
        shouldActivate = true;
        this.registry = (NamespaceRegistry) registry;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean canRun() {
        return shouldActivate;
    }

    @Override
    public void reset() {
        shouldActivate = false;
    }

    @Override
    public NamespaceRegistry additionalRegistry() {
        return registry;
    }

    @Override
    public void close() {
        bus.unsubscribe(id, this::activate);
    }

    @Override
    public String toString() {
        return "bus: " + getId();
    }
}
