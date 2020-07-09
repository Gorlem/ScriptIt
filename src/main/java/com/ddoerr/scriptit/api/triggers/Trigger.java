package com.ddoerr.scriptit.api.triggers;

import com.ddoerr.scriptit.api.Identifiable;

import java.util.Map;
import java.util.function.Consumer;

public interface Trigger extends Identifiable {
    void setCallback(Consumer<TriggerMessage> callback);
    void check();
    void close();

    Map<String, String> getData();
    void setData(Map<String, String> data);
}
