package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import com.google.gson.*;
import com.ddoerr.scriptit.scripts.ScriptBinding;
import net.fabricmc.fabric.mixin.client.keybinding.KeyCodeAccessor;
import net.minecraft.client.options.KeyBinding;

import java.lang.reflect.Type;

public class ScriptBindingAdapter implements JsonSerializer<ScriptBinding>, JsonDeserializer<ScriptBinding> {
    @Override
    public JsonElement serialize(ScriptBinding src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("id", src.getKeyBinding().getId());
        obj.addProperty("keyCode", ((KeyCodeAccessor)src.getKeyBinding()).getKeyCode().getKeyCode());
        obj.addProperty("content", src.getScriptContent());

        return obj;
    }

    @Override
    public ScriptBinding deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String id = jsonObject.getAsJsonPrimitive("id").getAsString();
        int keyCode = jsonObject.getAsJsonPrimitive("keyCode").getAsInt();
        String content = jsonObject.getAsJsonPrimitive("content").getAsString();

        KeyBinding keyBinding = KeyBindingHelper.create(id, keyCode);

        return new ScriptBinding(keyBinding, content);
    }
}
