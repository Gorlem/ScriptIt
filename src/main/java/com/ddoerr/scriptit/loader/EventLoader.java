package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.api.events.EventInitializer;
import com.ddoerr.scriptit.api.events.EventRegistry;
import com.ddoerr.scriptit.dependencies.Loadable;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.events.EventManager;
import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventLoader implements EventRegistry, Loadable {
    Map<String, Event> dispatchers = new HashMap<>();
    EventManager eventManager;

    public void load() {
        eventManager = Resolver.getInstance().resolve(EventManager.class);

        List<EventInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints("scriptit:event", EventInitializer.class);

        for (EventInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    @Override
    public Event registerEvent(String name) {
        Event event = new EventContainer(name, eventManager);
        dispatchers.put(name, event);

        return event;
    }

    public Map<String, Event> getDispatchers() {
        return dispatchers;
    }

    public Event findByName(String name) {
        return dispatchers.get(name);
    }
}
