package ml.gorlem.scriptit.loader;

import ml.gorlem.scriptit.dependencies.Loadable;
import ml.gorlem.scriptit.api.events.Event;
import ml.gorlem.scriptit.api.events.EventInitializer;
import ml.gorlem.scriptit.api.events.EventRegistry;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.events.EventManager;
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
