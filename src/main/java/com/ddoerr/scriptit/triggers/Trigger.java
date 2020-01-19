package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;

public interface Trigger {
    boolean canRun();
    void reset();
    NamespaceRegistry additionalRegistry();
}
