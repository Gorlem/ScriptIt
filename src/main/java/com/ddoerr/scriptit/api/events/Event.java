package com.ddoerr.scriptit.api.events;

import com.ddoerr.scriptit.api.libraries.Model;

import java.util.function.Consumer;

public interface Event {
    void registerListener(Consumer<Model> modelSupplier);
}
