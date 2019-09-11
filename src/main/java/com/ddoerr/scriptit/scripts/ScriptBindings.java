package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import com.ddoerr.scriptit.mixin.KeyBindingAccessor;
import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScriptBindings implements Tickable {
    List<ScriptBinding> scriptBindingList = new ArrayList<>();

    public void add(ScriptBinding scriptBinding) {
        scriptBindingList.add(scriptBinding);
    }

    public ScriptBinding addNew() {
        UUID uuid = UUID.randomUUID();
        KeyBinding keyBinding = KeyBindingHelper.create(new Identifier(ScriptItMod.MOD_NAME, uuid.toString()), -1);

        ScriptBinding scriptBinding = new ScriptBinding(keyBinding, StringUtils.EMPTY);
        add(scriptBinding);
        return scriptBinding;
    }

    public void remove(ScriptBinding scriptBinding) {
        scriptBindingList.remove(scriptBinding);
        Map<String, KeyBinding> keyBindings = KeyBindingAccessor.getKeysById();
        keyBindings.remove(scriptBinding.getId());
        KeyBinding.updateKeysByCode();

        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
    }

    public List<ScriptBinding> getAll() {
        return scriptBindingList;
    }

    @Override
    public void tick() {
        for (ScriptBinding scriptBinding : scriptBindingList) {
            if (scriptBinding.wasPressed()) {
                scriptBinding.run();
            }
        }
    }
}
