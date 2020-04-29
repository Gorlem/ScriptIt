package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.api.bus.EventBus;
import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import net.minecraft.util.Identifier;

public class EventManagerImpl implements Loadable {
    ScriptItRegistry registry;
    private EventBus eventBus;

    public EventManagerImpl(ScriptItRegistry registry, EventBus eventBus) {
        this.registry = registry;
        this.eventBus = eventBus;
    }

    @Override
    public void load() {
        for (Event event : registry.events) {
            Identifier identifier = registry.events.getId(event);

            event.registerListener(model -> {
                eventBus.publish(identifier.toString(), model);
            });
        }
    }
}
