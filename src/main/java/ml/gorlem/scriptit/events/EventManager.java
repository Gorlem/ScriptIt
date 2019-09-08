package ml.gorlem.scriptit.events;

import ml.gorlem.scriptit.api.events.Event;
import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import ml.gorlem.scriptit.api.scripts.Script;
import ml.gorlem.scriptit.api.scripts.ScriptBuilder;
import ml.gorlem.scriptit.callbacks.ConfigCallback;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.loader.EventContainer;
import ml.gorlem.scriptit.loader.EventLoader;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EventManager {
    Map<String, EventBinding> events = new HashMap<>();

    public void setContent(Event event, String content) {
        EventBinding eventBinding = new EventBinding(event, content);
        events.put(eventBinding.getId(), eventBinding);

        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
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
