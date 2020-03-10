package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.events.EventInitializer;
import com.ddoerr.scriptit.api.events.EventRegistry;
import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.events.Event;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventLoader implements EventRegistry, Loadable {
    List<Event> events = new ArrayList<>();

    public void load() {
        List<EventInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints(new Identifier(ScriptItMod.MOD_NAME, "event").toString(), EventInitializer.class);

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

    public List<String> getEvents() {
        return events.stream().map(Event::getName).collect(Collectors.toList());
    }
}
