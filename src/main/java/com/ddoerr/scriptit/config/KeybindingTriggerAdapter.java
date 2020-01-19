package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import com.ddoerr.scriptit.triggers.KeybindingTrigger;
import com.google.gson.*;
import net.fabricmc.fabric.mixin.client.keybinding.KeyCodeAccessor;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.lang.reflect.Type;

public class KeybindingTriggerAdapter implements JsonSerializer<KeybindingTrigger>, JsonDeserializer<KeybindingTrigger> {
    @Override
    public KeybindingTrigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String keyCode = jsonObject.getAsJsonPrimitive("keyCode").getAsString();

        return new KeybindingTrigger(InputUtil.fromName(keyCode));
    }

    @Override
    public JsonElement serialize(KeybindingTrigger src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("keyCode", src.getName());

        return obj;
    }
}