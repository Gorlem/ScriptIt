package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.*;
import java.util.function.Consumer;

public class KeyBindingManagerImpl implements KeyBindingManager {
    protected Map<String, Pair<KeyBinding, List<Consumer<TriggerMessage>>>> listeners = new HashMap<>();

    @Override
    public void registerListener(String keyName, Consumer<TriggerMessage> messageConsumer) {
        Pair<KeyBinding, List<Consumer<TriggerMessage>>> pair = listeners.get(keyName);

        if (pair == null) {
            Identifier identifier = new Identifier(ScriptItMod.MOD_NAME, UUID.randomUUID().toString());
            InputUtil.KeyCode keyCode = InputUtil.fromName(keyName);

            KeyBinding keyBinding = KeyBindingHelper.create(identifier, keyCode);

            pair = new Pair<>(keyBinding, new ArrayList<>());
            listeners.put(keyName, pair);
        }

        pair.getRight().add(messageConsumer);
    }

    @Override
    public void removeListener(String keyName, Consumer<TriggerMessage> messageConsumer) {
        Pair<KeyBinding, List<Consumer<TriggerMessage>>> pair = listeners.get(keyName);
        if (pair != null) {
            pair.getRight().remove(messageConsumer);

            if (pair.getRight().isEmpty()) {
                KeyBindingHelper.remove(pair.getLeft());
                listeners.remove(keyName);
            }
        }
    }

    @Override
    public void tick() {
        for (Pair<KeyBinding, List<Consumer<TriggerMessage>>> pair : listeners.values()) {
            if (pair.getLeft().wasPressed()) {
                for (Consumer<TriggerMessage> consumer : pair.getRight()) {
                    consumer.accept(new TriggerMessageImpl());
                }
            }
        }
    }
}
