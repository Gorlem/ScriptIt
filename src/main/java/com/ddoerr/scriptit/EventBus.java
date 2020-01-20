package com.ddoerr.scriptit;

import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;

import java.util.*;
import java.util.function.Consumer;

public class EventBus implements Bus<Object> {
    Map<String, List<Consumer<Object>>> subscribers = new HashMap<>();

    @Override
    public void subscribe(String id, Consumer<Object> consumer) {
        if (subscribers.containsKey(id)) {
            subscribers.get(id).add(consumer);
        } else {
            subscribers.put(id, new ArrayList<>(Collections.singletonList(consumer)));
            publish("bus:added", id);
        }

        publish("bus:subscribed", id);
    }

    @Override
    public void unsubscribe(String id, Consumer<Object> consumer) {
        if (!subscribers.containsKey(id)) {
            return;
        }

        subscribers.get(id).remove(consumer);
        publish("bus:unsubscribed", id);

        if (subscribers.get(id).isEmpty()) {
            subscribers.remove(id);
            publish("bus:removed", id);
        }
    }

    @Override
    public void publish(String id, Object data) {
        if (!subscribers.containsKey(id)) {
            return;
        }

        for (Consumer<Object> consumer : subscribers.get(id)) {
            consumer.accept(data);
        }
    }
}
