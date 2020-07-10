package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.triggers.KeyBindingManager;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class KeyBindingTrigger implements Trigger {
    public static final Identifier IDENTIFIER = new Identifier(ScriptItMod.MOD_NAME, "keybinding");
    public static final String KEYNAME = "key";

    private String keyName;
    private Consumer<TriggerMessage> callback = triggerMessage -> {};

    private KeyBindingManager keyBindingManager;

    public KeyBindingTrigger(KeyBindingManager keyBindingManager) {
        this.keyBindingManager = keyBindingManager;
    }

    @Override
    public void setCallback(Consumer<TriggerMessage> callback) {
        close();
        this.callback = callback;
        if (keyName != null) {
            keyBindingManager.registerListener(keyName, callback);
        }
    }

    @Override
    public void check() { }

    @Override
    public void close() {
        if (keyName != null) {
            keyBindingManager.removeListener(keyName, callback);
        }
    }

    @Override
    public Map<String, String> getData() {
        return Collections.singletonMap(KEYNAME, keyName);
    }

    @Override
    public void setData(Map<String, String> data) {
        close();
        keyName = data.get(KEYNAME);
        if (keyName != null) {
            keyBindingManager.registerListener(keyName, callback);
        }
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    public String getKeyName() {
        return keyName;
    }

    @Override
    public String toString() {
        return "on key " + KeyBindingHelper.getKeyCodeName(InputUtil.fromName(keyName));
    }
}
