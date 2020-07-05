package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.api.bus.Bus;
import com.ddoerr.scriptit.api.bus.EventBus;
import com.ddoerr.scriptit.api.bus.KeyBindingBusExtension;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;

import java.util.function.Consumer;

public class BusTrigger implements Trigger {
    String id;
    Bus<Object> bus;
    Consumer<TriggerMessage> callback = model -> {};
    Consumer<Object> busMessage = object -> callback.accept((TriggerMessage) object);

    public BusTrigger(String id) {
        this.id = id;
        try {
            bus = Resolver.getInstance().resolve(EventBus.class);
            bus.subscribe(this.id, busMessage);
        } catch (DependencyException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    @Override
    public void close() {
        bus.unsubscribe(id, busMessage);
    }

    @Override
    public void setCallback(Consumer<TriggerMessage> callback) {
        this.callback = callback;
    }

    @Override
    public void check() {
    }

    @Override
    public String toString() {
        if (KeyBindingBusExtension.isKeyEvent(id)) {
            return "on key " + KeyBindingHelper.getKeyCodeName(InputUtil.fromName(id));
        }
        return "on event " + id;
    }
}
