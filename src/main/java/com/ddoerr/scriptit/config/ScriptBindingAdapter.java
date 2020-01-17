package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.LifeCycle;
import com.ddoerr.scriptit.ScriptContainer;
import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import com.ddoerr.scriptit.triggers.KeybindingTrigger;
import com.google.gson.*;
import com.ddoerr.scriptit.scripts.ScriptBinding;
import net.fabricmc.fabric.mixin.client.keybinding.KeyCodeAccessor;
import net.minecraft.client.options.KeyBinding;

import java.lang.reflect.Type;

public class ScriptBindingAdapter implements JsonSerializer<ScriptContainer>, JsonDeserializer<ScriptContainer> {
    @Override
    public JsonElement serialize(ScriptContainer src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        KeyBinding keyBinding = ((KeybindingTrigger)src.getTrigger()).getKeyBinding();

        obj.addProperty("id", keyBinding.getId());
        obj.addProperty("keyCode", ((KeyCodeAccessor)keyBinding).getKeyCode().getKeyCode());
        obj.addProperty("content", src.getContent());

        return obj;
    }

    @Override
    public ScriptContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String id = jsonObject.getAsJsonPrimitive("id").getAsString();
        int keyCode = jsonObject.getAsJsonPrimitive("keyCode").getAsInt();
        String content = jsonObject.getAsJsonPrimitive("content").getAsString();

        KeyBinding keyBinding = KeyBindingHelper.create(id, keyCode);

        return new ScriptContainer(new KeybindingTrigger(keyBinding), LifeCycle.Threaded, content);
    }
}
