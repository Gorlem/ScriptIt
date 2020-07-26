package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.fields.Field;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractTrigger implements Trigger {
    protected Consumer<TriggerMessage> callback;
    protected Map<String, Field<?>> fields = new LinkedHashMap<>();

    @Override
    public void setCallback(Consumer<TriggerMessage> callback) {
        stop();
        this.callback = callback;
        start();
    }

    @Override
    public void check() {
        // Empty default
    }

    @Override
    public void start() {
        // Empty default
    }

    @Override
    public void stop() {
        // Empty default
    }

    @Override
    public Map<String, Field<?>> getFields() {
        return fields;
    }
}
