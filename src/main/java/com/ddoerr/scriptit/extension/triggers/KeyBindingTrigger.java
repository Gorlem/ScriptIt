package com.ddoerr.scriptit.extension.triggers;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.triggers.KeyBindingManager;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import com.ddoerr.scriptit.fields.Field;
import com.ddoerr.scriptit.fields.KeyBindingField;
import com.ddoerr.scriptit.fields.StringField;
import com.ddoerr.scriptit.triggers.AbstractTrigger;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class KeyBindingTrigger extends AbstractTrigger {
    public static final Identifier IDENTIFIER = new Identifier(ScriptItMod.MOD_NAME, "keybinding");
    public static final String KEY_FIELD = "key";

    private KeyBindingManager keyBindingManager;

    private KeyBindingField keyField;

    public KeyBindingTrigger(KeyBindingManager keyBindingManager) {
        this.keyBindingManager = keyBindingManager;

        keyField = new KeyBindingField();
        keyField.setTitle(new LiteralText("Key"));
        keyField.setDescription(new LiteralText("Key used to trigger"));
        fields.put(KEY_FIELD, keyField);
    }

    @Override
    public void start() {
        String keyName = keyField.serialize();
        if (keyName != null) {
            keyBindingManager.registerListener(keyName, callback);
        }
    }

    @Override
    public void stop() {
        String keyName = keyField.serialize();
        if (keyName != null) {
            keyBindingManager.removeListener(keyName, callback);
        }
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public String toString() {
        return "on key " + KeyBindingHelper.getKeyCodeName(keyField.getValue());
    }
}
