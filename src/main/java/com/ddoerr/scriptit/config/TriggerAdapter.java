package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.triggers.ContinuousTrigger;
import com.ddoerr.scriptit.triggers.KeybindingTrigger;
import com.ddoerr.scriptit.triggers.EventTrigger;
import com.ddoerr.scriptit.triggers.Trigger;
import com.google.gson.*;

import java.lang.reflect.Type;

public class TriggerAdapter implements JsonDeserializer<Trigger> {
    @Override
    public Trigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("keyCode")) {
            return context.deserialize(json, KeybindingTrigger.class);
        }

        if (jsonObject.has("event")) {
            return context.deserialize(json, EventTrigger.class);
        }

        if (jsonObject.has("duration")) {
            return context.deserialize(json, ContinuousTrigger.class);
        }

        return null;
    }
}
