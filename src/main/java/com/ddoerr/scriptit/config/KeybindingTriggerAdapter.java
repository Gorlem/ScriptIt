package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import com.ddoerr.scriptit.triggers.KeybindingTrigger;
import com.google.gson.*;
import net.fabricmc.fabric.mixin.client.keybinding.KeyCodeAccessor;
import net.minecraft.client.options.KeyBinding;

import java.lang.reflect.Type;

public class KeybindingTriggerAdapter implements JsonSerializer<KeybindingTrigger>, JsonDeserializer<KeybindingTrigger> {
    @Override
    public KeybindingTrigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int keyCode = jsonObject.getAsJsonPrimitive("keyCode").getAsInt();
        String id = jsonObject.getAsJsonPrimitive("id").getAsString();

        KeyBinding keyBinding = KeyBindingHelper.create(id, keyCode);

        return new KeybindingTrigger(keyBinding);
    }

    @Override
    public JsonElement serialize(KeybindingTrigger src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("id", src.getKeyBinding().getId());
        obj.addProperty("keyCode", ((KeyCodeAccessor)src.getKeyBinding()).getKeyCode().getKeyCode());

        return obj;
    }
}
