package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.triggers.BusTrigger;
import com.google.gson.*;

import java.lang.reflect.Type;

public class BusTriggerAdapter implements JsonSerializer<BusTrigger>, JsonDeserializer<BusTrigger> {
    @Override
    public BusTrigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String bus = jsonObject.getAsJsonPrimitive("bus").getAsString();

        return new BusTrigger(bus);
    }

    @Override
    public JsonElement serialize(BusTrigger src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("bus", src.getId());

        return obj;
    }
}
