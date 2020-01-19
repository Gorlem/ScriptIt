package com.ddoerr.scriptit;

import java.util.*;
import java.util.function.Consumer;

public class EventBus {
    Map<String, List<Consumer<Object>>> subscribers = new HashMap<>();

    public void subscribe(String id, Consumer<Object> consumer) {
        if (subscribers.containsKey(id)) {
            subscribers.get(id).add(consumer);
        } else {
            subscribers.put(id, new ArrayList<>(Collections.singletonList(consumer)));
        }
    }

    public void unsubscribe(String id, Consumer<Object> consumer) {
        if (!subscribers.containsKey(id)) {
            return;
        }

        subscribers.get(id).remove(consumer);
    }

    public void publish(String id, Object data) {
        if (!subscribers.containsKey(id)) {
            return;
        }

        for (Consumer<Object> consumer : subscribers.get(id)) {
            consumer.accept(data);
        }
    }
}
