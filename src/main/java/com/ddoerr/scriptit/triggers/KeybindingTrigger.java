package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import net.minecraft.client.options.KeyBinding;

public class KeybindingTrigger implements Trigger {
    KeyBinding keyBinding;

    public KeybindingTrigger(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }

    @Override
    public boolean canRun() {
        return keyBinding.wasPressed();
    }

    @Override
    public void reset() { }

    @Override
    public NamespaceRegistry additionalRegistry() {
        return null;
    }

    public KeyBinding getKeyBinding() {
        return keyBinding;
    }
}
