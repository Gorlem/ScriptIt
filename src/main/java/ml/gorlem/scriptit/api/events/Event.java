package ml.gorlem.scriptit.api.events;

import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;

public interface Event {
    void dispatch();
    void dispatch(NamespaceRegistry registry);

    String getName();
}
