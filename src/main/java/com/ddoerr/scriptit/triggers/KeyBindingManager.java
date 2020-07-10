package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import net.minecraft.util.Tickable;

import java.util.function.Consumer;

public interface KeyBindingManager extends Tickable {
    void registerListener(String keyName, Consumer<TriggerMessage> messageConsumer);

    void removeListener(String keyName, Consumer<TriggerMessage> messageConsumer);
}
