package com.ddoerr.scriptit.api.events;

import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;

public interface Event {
    void dispatch();
    void dispatch(NamespaceRegistry registry);

    String getName();
}
