package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class KeybindingTrigger implements Trigger {
    InputUtil.KeyCode keyCode;
    KeyBinding keyBinding;

    public KeybindingTrigger(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }

    public KeybindingTrigger(InputUtil.KeyCode keyCode) {
        this.keyCode = keyCode;
        KeyBinding keyBinding = KeyBindingHelper.create(new Identifier(ScriptItMod.MOD_NAME, UUID.randomUUID().toString()));
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

    @Override
    public void close() {

    }

    public KeyBinding getKeyBinding() {
        return keyBinding;
    }
}
