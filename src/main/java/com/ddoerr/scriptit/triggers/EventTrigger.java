package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class EventTrigger implements Trigger {
    public static final Identifier IDENTIFIER = new Identifier(ScriptItMod.MOD_NAME, "event");
    public static final String EVENTNAME = "event";

    private Consumer<TriggerMessage> callback = triggerMessage -> {};

    private Identifier eventIdentifier;

    private ScriptItRegistry registry;

    public EventTrigger(ScriptItRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void setCallback(Consumer<TriggerMessage> callback) {
        close();

        this.callback = callback;
        Event event = registry.events.get(eventIdentifier);
        if (event != null) {
            event.registerListener(callback);
        }
    }

    @Override
    public void check() { }

    @Override
    public void close() {
        Event event = registry.events.get(eventIdentifier);

        if (event != null) {
            event.removeListener(callback);
        }
    }

    @Override
    public Map<String, String> getData() {
        return Collections.singletonMap(EVENTNAME, eventIdentifier.toString());
    }

    @Override
    public void setData(Map<String, String> data) {
        close();

        eventIdentifier = new Identifier(data.get(EVENTNAME));
        Event event = registry.events.get(eventIdentifier);
        if (event != null) {
            event.registerListener(callback);
        }
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    public Identifier getEventIdentifier() {
        return eventIdentifier;
    }

    @Override
    public String toString() {
        return "on event " + eventIdentifier.toString();
    }
}
