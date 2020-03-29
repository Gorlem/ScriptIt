package com.ddoerr.scriptit.api.bus;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KeyBindingBusExtension implements Tickable{
    EventBus eventBus;
    Map<String, KeyBinding> keyBindings = new HashMap<>();

    public KeyBindingBusExtension(EventBus eventBus) {
        eventBus.subscribe("bus:added", this::newEvent);
        eventBus.subscribe("bus:removed", this::removedEvent);
        this.eventBus = eventBus;
    }

    private void newEvent(Object idObject) {
        String id = (String) idObject;

        if (!isKeyEvent(id)) {
            return;
        }

        InputUtil.KeyCode keyCode = InputUtil.fromName(id);
        KeyBinding keyBinding = KeyBindingHelper.create(new Identifier(ScriptItMod.MOD_NAME, UUID.randomUUID().toString()), keyCode);

        keyBindings.put(id, keyBinding);
    }

    private void removedEvent(Object idObject) {
        String id = (String) idObject;

        if (!isKeyEvent(id)) {
            return;
        }

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

    public static boolean isKeyEvent(String eventId) {
        return eventId.startsWith("key.");
    }
}
