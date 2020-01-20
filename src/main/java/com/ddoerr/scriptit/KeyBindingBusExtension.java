package com.ddoerr.scriptit;

import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import com.ddoerr.scriptit.dependencies.Resolver;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;

import java.util.*;
import java.util.function.Consumer;

public class KeyBindingBusExtension implements Tickable{
    EventBus eventBus;

    Map<String, List<Consumer<Object>>> subscribers = new HashMap<>();
    Map<String, KeyBinding> keyBindings = new HashMap<>();

    public KeyBindingBusExtension() {
        eventBus = Resolver.getInstance().resolve(EventBus.class);
        eventBus.subscribe("bus:new", this::newEvent);
        eventBus.subscribe("bus:removed", this::removedEvent);
    }

    private void newEvent(Object idObject) {
        String id = (String) idObject;

        int keyCode = InputUtil.fromName(id).getKeyCode();
        KeyBinding keyBinding = KeyBindingHelper.create(new Identifier(ScriptItMod.MOD_NAME, UUID.randomUUID().toString()), keyCode);

        keyBindings.put(id, keyBinding);
    }

    private void removedEvent(Object idObject) {
        String id = (String) idObject;

        KeyBindingHelper.remove(keyBindings.get(id));
        keyBindings.remove(id);
    }

    @Override
    public void tick() {
        for (Map.Entry<String, KeyBinding> entry : keyBindings.entrySet()) {
            KeyBinding keyBinding = entry.getValue();

            if (keyBinding.wasPressed()) {
                eventBus.publish(entry.getKey(), null);
            }
        }
    }
}
