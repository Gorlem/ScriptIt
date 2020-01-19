package com.ddoerr.scriptit;

import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import com.ddoerr.scriptit.dependencies.Resolver;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;

import java.util.*;
import java.util.function.Consumer;

public class KeyBindingBus implements Tickable, Bus<Object> {
    EventBus eventBus;

    Map<String, List<Consumer<Object>>> subscribers = new HashMap<>();
    Map<String, KeyBinding> keyBindings = new HashMap<>();

    public KeyBindingBus() {
        eventBus = Resolver.getInstance().resolve(EventBus.class);
    }

    @Override
    public void tick() {
        for (Map.Entry<String, KeyBinding> entry : keyBindings.entrySet()) {
            KeyBinding keyBinding = entry.getValue();

            if (keyBinding.wasPressed()) {
                publish(entry.getKey(), null);
            }
        }
    }

    @Override
    public void subscribe(String id, Consumer<Object> consumer) {
        if (subscribers.containsKey(id)) {
            subscribers.get(id).add(consumer);
        } else {
            int keyCode = InputUtil.fromName(id).getKeyCode();
            KeyBinding keyBinding = KeyBindingHelper.create(new Identifier(ScriptItMod.MOD_NAME, UUID.randomUUID().toString()), keyCode);

            subscribers.put(id, new ArrayList<>(Collections.singletonList(consumer)));
            keyBindings.put(id, keyBinding);
        }
    }

    @Override
    public void unsubscribe(String id, Consumer<Object> consumer) {
        if (!subscribers.containsKey(id)) {
            return;
        }

        subscribers.get(id).remove(consumer);

        if (subscribers.get(id).isEmpty()) {
            KeyBindingHelper.remove(keyBindings.get(id));

            subscribers.remove(id);
            keyBindings.remove(id);
        }
    }

    @Override
    public void publish(String id, Object data) {
        for (Consumer<Object> consumer : subscribers.get(id)) {
            consumer.accept(data);
        }
    }
}
