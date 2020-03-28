package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.bus.Bus;
import com.ddoerr.scriptit.api.bus.EventBus;
import com.ddoerr.scriptit.api.bus.KeyBindingBusExtension;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.libraries.Library;
import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;

import java.util.function.Consumer;

public class BusTrigger implements Trigger, Consumer<Object> {
    boolean shouldActivate = false;
    Library library = null;
    String id;
    Bus<Object> bus;

    public BusTrigger(String id) {
        this.id = id;
        try {
            bus = Resolver.getInstance().resolve(EventBus.class);
            bus.subscribe(this.id, this);
        } catch (DependencyException e) {
            e.printStackTrace();
        }
    }

    public void activate(Library library) {
        shouldActivate = true;
        this.library = library;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean canRun() {
        return shouldActivate;
    }

    @Override
    public void reset() {
        shouldActivate = false;
    }

    @Override
    public Library getAdditionalLibrary() {
        return library;
    }

    @Override
    public void close() {
        bus.unsubscribe(id, this);
    }

    @Override
    public String toString() {
        if (KeyBindingBusExtension.isKeyEvent(id)) {
            return "on key " + KeyBindingHelper.getKeyCodeName(InputUtil.fromName(id));
        }
        return "on event " + id;
    }

    @Override
    public void accept(Object o) {
        activate((Library) o);
    }
}
