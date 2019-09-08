package ml.gorlem.scriptit.loader;

import ml.gorlem.scriptit.api.events.Event;
import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import ml.gorlem.scriptit.api.scripts.ScriptBuilder;
import ml.gorlem.scriptit.callbacks.ChatMessageCallback;
import ml.gorlem.scriptit.events.EventManager;
import net.fabricmc.fabric.api.event.EventFactory;

public class EventContainer implements Event {
    String name;
    EventManager eventManager;

    public EventContainer(String name, EventManager eventManager) {
        this.name = name;
        this.eventManager = eventManager;
    }

    @Override
    public void dispatch() {
        eventManager.dispatch(this, null);
    }

    @Override
    public void dispatch(NamespaceRegistry registry) {
        eventManager.dispatch(this, registry);
    }

    public String getName() {
        return name;
    }
}
