package com.ddoerr.scriptit.loader.container;

import com.ddoerr.scriptit.api.bus.EventBus;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.libraries.Model;

public class EventImpl implements Event {
    String name;
    EventBus eventBus;

    public EventImpl(String name) {
        this.name = name;
        try {
            eventBus = Resolver.getInstance().resolve(EventBus.class);
        } catch (DependencyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispatch() {
        eventBus.publish(name, null);
    }

    @Override
    public void dispatch(Model model) {
        eventBus.publish(name, model);
    }

    public String getName() {
        return name;
    }
}
