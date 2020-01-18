package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.triggers.KeybindingTrigger;
import com.ddoerr.scriptit.triggers.ManualTrigger;
import com.ddoerr.scriptit.triggers.Trigger;
import com.google.gson.*;

import java.lang.reflect.Type;

public class TriggerAdapter implements JsonDeserializer<Trigger> {
    @Override
    public Trigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("keyCode") && jsonObject.has("id")) {
            return context.deserialize(json, KeybindingTrigger.class);
        }

        return context.deserialize(json, ManualTrigger.class);
    }
}
