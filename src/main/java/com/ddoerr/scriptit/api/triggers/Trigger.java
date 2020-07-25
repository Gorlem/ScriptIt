package com.ddoerr.scriptit.api.triggers;

import com.ddoerr.scriptit.api.Identifiable;
import com.ddoerr.scriptit.fields.Field;

import java.util.Map;
import java.util.function.Consumer;

public interface Trigger extends Identifiable {
    void setCallback(Consumer<TriggerMessage> callback);
    void check();

    void start();
    void stop();

    Map<String, Field<?>> getFields();
}
