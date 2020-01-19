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
    Bus<NamespaceRegistry> bus;

    public BusTrigger(String busName, String id) {
        this.id = id;

        Collection<Bus> buses = Resolver.getInstance().resolveAll(Bus.class);
        bus = buses.stream().filter(b -> b.getClass().getSimpleName().equals(busName)).findAny().get();

        bus.subscribe(this.id, this::activate);
    }

    public void activate(NamespaceRegistry registry) {
        shouldActivate = true;
        this.registry = registry;
    }

    public String getId() {
        return id;
    }

    public String getBusName() {
        return bus.getClass().getSimpleName();
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
}
