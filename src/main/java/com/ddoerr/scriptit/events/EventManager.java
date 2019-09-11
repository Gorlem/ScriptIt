package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EventManager {
    Map<String, EventBinding> events = new HashMap<>();

    public void setContent(Event event, String content) {
        EventBinding eventBinding = events.getOrDefault(event.getName(), new EventBinding(event));
        eventBinding.setScriptContent(content);

        events.put(event.getName(), eventBinding);
    }

    public void dispatch(Event event, NamespaceRegistry registry) {
        EventBinding eventBinding = events.getOrDefault(event.getName(), null);

        if (eventBinding == null)
            return;

        eventBinding.run(registry);
    }

    public String getContent(Event event) {
        EventBinding eventBinding = events.getOrDefault(event.getName(), null);

        if (eventBinding == null)
            return StringUtils.EMPTY;

        return eventBinding.getScriptContent();
    }

    public Collection<EventBinding> getAll() {
        return events.values();
    }
}
