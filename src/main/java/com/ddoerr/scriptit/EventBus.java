package com.ddoerr.scriptit;

import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;

import java.util.*;
import java.util.function.Consumer;

public class EventBus implements Bus<NamespaceRegistry> {
    Map<String, List<Consumer<NamespaceRegistry>>> subscribers = new HashMap<>();

    @Override
    public void subscribe(String id, Consumer<NamespaceRegistry> consumer) {
        if (subscribers.containsKey(id)) {
            subscribers.get(id).add(consumer);
        } else {
            subscribers.put(id, new ArrayList<>(Collections.singletonList(consumer)));
        }
    }

    @Override
    public void unsubscribe(String id, Consumer<NamespaceRegistry> consumer) {
        if (!subscribers.containsKey(id)) {
            return;
        }

        subscribers.get(id).remove(consumer);
    }

    @Override
    public void publish(String id, NamespaceRegistry data) {
        if (!subscribers.containsKey(id)) {
            return;
        }

        for (Consumer<NamespaceRegistry> consumer : subscribers.get(id)) {
            consumer.accept(data);
        }
    }
}
