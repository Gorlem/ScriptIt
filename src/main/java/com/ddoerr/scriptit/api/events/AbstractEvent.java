package com.ddoerr.scriptit.api.events;

import com.ddoerr.scriptit.api.libraries.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AbstractEvent implements Event {
    protected List<Consumer<Model>> listeners = new ArrayList<>();

    @Override
    public void registerListener(Consumer<Model> modelSupplier) {
        listeners.add(modelSupplier);
    }

    protected void dispatch() {
        dispatch(null);
    }

    protected void dispatch(Model model) {
        for (Consumer<Model> listener : listeners) {
            listener.accept(model);
        }
    }
}
