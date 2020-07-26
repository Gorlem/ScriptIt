package com.ddoerr.scriptit.extension.triggers;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerFactory;

public class KeyBindingTriggerFactory implements TriggerFactory {
    private Resolver resolver;

    public KeyBindingTriggerFactory(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public Trigger createTrigger() {
        try {
            return resolver.create(KeyBindingTrigger.class);
        } catch (DependencyException e) {
            e.printStackTrace();
            return null;
        }
    }

}
