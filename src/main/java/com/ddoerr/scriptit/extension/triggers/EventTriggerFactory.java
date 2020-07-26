package com.ddoerr.scriptit.extension.triggers;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerFactory;

public class EventTriggerFactory implements TriggerFactory {
    private Resolver resolver;

    public EventTriggerFactory(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public Trigger createTrigger() {
        try {
            return resolver.create(EventTrigger.class);
        } catch (DependencyException e) {
            e.printStackTrace();
            return null;
        }
    }

}
