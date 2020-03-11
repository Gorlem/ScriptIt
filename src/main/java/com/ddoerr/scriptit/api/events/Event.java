package com.ddoerr.scriptit.api.events;

import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;

public interface Event {
    void dispatch();
    NamespaceRegistry createNamespace();
    String getName();
}
