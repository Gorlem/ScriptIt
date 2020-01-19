package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.KeyBindingBus;
import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import com.ddoerr.scriptit.dependencies.Resolver;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class KeybindingTrigger implements Trigger {
    boolean shouldActivate = false;
    InputUtil.KeyCode keyCode;
    KeyBindingBus keyBindingBus;

    public KeybindingTrigger(InputUtil.KeyCode keyCode) {
        this.keyCode = keyCode;

        keyBindingBus = Resolver.getInstance().resolve(KeyBindingBus.class);
        keyBindingBus.subscribe(keyCode.getName(), this::activate);
    }

    public void activate(Object object) {
        shouldActivate = true;
    }

    @Override
    public boolean canRun() {
        return shouldActivate;
    }

    @Override
    public void reset() {
        shouldActivate = false;
    }

    public String getName() {
        return keyCode.getName();
    }

    @Override
    public NamespaceRegistry additionalRegistry() {
        return null;
    }

    @Override
    public void close() {
        keyBindingBus.unsubscribe(keyCode.getName(), this::activate);
    }
}
