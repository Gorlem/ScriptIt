package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.triggers.ManualTrigger;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ManualTriggerAdapter implements JsonSerializer<ManualTrigger>, JsonDeserializer<ManualTrigger> {
    @Override
    public ManualTrigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new ManualTrigger();
    }

    @Override
    public JsonElement serialize(ManualTrigger src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonObject();
    }
}
