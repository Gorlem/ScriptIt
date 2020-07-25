package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerFactory;

public class DurationTriggerFactory implements TriggerFactory {
    private Resolver resolver;

    public DurationTriggerFactory(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public Trigger createTrigger() {
        try {
            return resolver.create(DurationTrigger.class);
        } catch (DependencyException e) {
            e.printStackTrace();
            return null;
        }
    }

}
