package com.ddoerr.scriptit.api.scripts;

public interface ThreadLifetimeManager {
    void watch(ScriptThread thread);

    int stopAll();
}
