package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.api.events.EventInitializer;
import com.ddoerr.scriptit.api.events.EventRegistry;
import com.ddoerr.scriptit.dependencies.Loadable;
import com.ddoerr.scriptit.api.events.Event;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class EventLoader implements EventRegistry, Loadable {
    List<Event> events = new ArrayList<>();

    public void load() {
        List<EventInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints("scriptit:event", EventInitializer.class);

        for (EventInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    @Override
    public Event registerEvent(String name) {
        Event event = new EventImpl(name);
        events.add(event);
        return event;
    }
}
