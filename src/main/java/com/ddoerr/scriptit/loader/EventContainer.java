package com.ddoerr.scriptit.loader;

import com.ddoerr.scriptit.ScriptContainer;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.triggers.ManualTrigger;

public class EventContainer implements Event {
    ScriptContainer scriptContainer;

    public EventContainer(ScriptContainer scriptContainer) {
        this.scriptContainer = scriptContainer;
    }

    @Override
    public void dispatch() {
        scriptContainer.setNamespaceRegistry(null);
        ((ManualTrigger)scriptContainer.getTrigger()).activate();
    }

    @Override
    public void dispatch(NamespaceRegistry registry) {
        scriptContainer.setNamespaceRegistry(registry);
        ((ManualTrigger)scriptContainer.getTrigger()).activate();
    }

    public String getName() {
        return scriptContainer.getName();
    }
}
