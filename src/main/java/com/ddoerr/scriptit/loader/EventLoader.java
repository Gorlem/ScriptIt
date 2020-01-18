package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.ScriptContainer;
import com.ddoerr.scriptit.Scripts;
import com.ddoerr.scriptit.api.events.EventInitializer;
import com.ddoerr.scriptit.api.events.EventRegistry;
import com.ddoerr.scriptit.dependencies.Loadable;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.triggers.ManualTrigger;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public class EventLoader implements EventRegistry, Loadable {
    private Scripts scripts;

    public void load() {
        scripts = Resolver.getInstance().resolve(Scripts.class);

        List<EventInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints("scriptit:event", EventInitializer.class);

        for (EventInitializer initializer : entrypoints) {
            initializer.onInitialize(this);
        }
    }

    @Override
    public Event registerEvent(String name) {
        ScriptContainer scriptContainer = new ScriptContainer(new ManualTrigger());
        scriptContainer.setName(name);
        Event event = new EventContainer(scriptContainer);
        scripts.add(Scripts.EVENT_CATEGORY, scriptContainer);

        return event;
    }
}
