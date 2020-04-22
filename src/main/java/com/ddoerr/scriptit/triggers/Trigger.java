package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.libraries.Model;

import java.util.function.Consumer;

public interface Trigger {
    void setCallback(Consumer<Model> callback);
    void check();
    void close();
}
