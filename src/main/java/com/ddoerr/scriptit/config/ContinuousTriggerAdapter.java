package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.triggers.ContinuousTrigger;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

public class ContinuousTriggerAdapter implements JsonSerializer<ContinuousTrigger>, JsonDeserializer<ContinuousTrigger> {
    @Override
    public ContinuousTrigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String duration = jsonObject.getAsJsonPrimitive("duration").getAsString();
        Duration parsed = Duration.parse(duration);

        return new ContinuousTrigger(parsed);
    }

    @Override
    public JsonElement serialize(ContinuousTrigger src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("duration", src.getDuration().toString());

        return obj;
    }
}
