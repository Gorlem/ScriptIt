package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import com.ddoerr.scriptit.api.bus.Bus;
import com.ddoerr.scriptit.api.bus.EventBus;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.bus.KeyBindingBusExtension;
import com.ddoerr.scriptit.dependencies.Resolver;
import net.minecraft.client.util.InputUtil;

import java.util.function.Consumer;

public class BusTrigger implements Trigger, Consumer<Object> {
    boolean shouldActivate = false;
    NamespaceRegistry registry = null;
    String id;
    Bus<Object> bus;

    public BusTrigger(String id) {
        this.id = id;
        bus = Resolver.getInstance().resolve(EventBus.class);
        bus.subscribe(this.id, this);
    }

    public void activate(NamespaceRegistry registry) {
        shouldActivate = true;
        this.registry = registry;
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
        bus.unsubscribe(id, this);
    }

    @Override
    public String toString() {
        if (KeyBindingBusExtension.isKeyEvent(id)) {
            return "on key " + KeyBindingHelper.getKeyCodeName(InputUtil.fromName(id));
        }
        return "on event " + id;
    }

    @Override
    public void accept(Object o) {
        activate((NamespaceRegistry) o);
    }
}
