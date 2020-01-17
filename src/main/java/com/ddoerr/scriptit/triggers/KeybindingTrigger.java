package com.ddoerr.scriptit.triggers;

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

    public KeyBinding getKeyBinding() {
        return keyBinding;
    }
}
