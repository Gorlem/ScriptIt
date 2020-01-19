package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.triggers.BusTrigger;
import com.ddoerr.scriptit.triggers.ContinuousTrigger;
import com.ddoerr.scriptit.triggers.Trigger;
import com.google.gson.*;

import java.lang.reflect.Type;

public class TriggerAdapter implements JsonDeserializer<Trigger> {
    @Override
    public Trigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("bus")) {
            return context.deserialize(json, BusTrigger.class);
        }

        if (jsonObject.has("duration")) {
            return context.deserialize(json, ContinuousTrigger.class);
        }

        return null;
    }
}
