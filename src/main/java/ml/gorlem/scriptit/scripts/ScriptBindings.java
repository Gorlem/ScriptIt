package ml.gorlem.scriptit.scripts;

import ml.gorlem.scriptit.ScriptItMod;
import ml.gorlem.scriptit.callbacks.ConfigCallback;
import ml.gorlem.scriptit.mixin.KeyBindingAccessor;
import ml.gorlem.scriptit.api.util.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.text.LiteralText;
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
