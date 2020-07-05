package com.ddoerr.scriptit.api.triggers;

import java.util.function.Consumer;

public interface Trigger {
    void setCallback(Consumer<TriggerMessage> callback);
    void check();
    void close();
}
