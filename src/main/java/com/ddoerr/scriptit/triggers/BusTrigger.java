package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.api.bus.Bus;
import com.ddoerr.scriptit.api.bus.EventBus;
import com.ddoerr.scriptit.api.bus.KeyBindingBusExtension;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BusTrigger implements Trigger {
    public static final Identifier IDENTIFIER = new Identifier(ScriptItMod.MOD_NAME, "bus");

    String id;
    Bus<Object> bus;
    Consumer<TriggerMessage> callback = model -> {};
    Consumer<Object> busMessage = object -> callback.accept((TriggerMessage) object);

    public BusTrigger(EventBus eventBus) {
        bus = eventBus;
    }

    public String getId() {
        return id;
    }

    @Override
    public void close() {
        bus.unsubscribe(id, busMessage);
    }

    @Override
    public Map<String, String> getData() {
        return Collections.singletonMap("id", id);
    }

    @Override
    public void setData(Map<String, String> data) {
        setId(data.get("id"));
    }

    public void setId(String id) {
        if (this.id != null) {
            close();
        }

        this.id = id;
        bus.subscribe(this.id, busMessage);
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

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }
}
