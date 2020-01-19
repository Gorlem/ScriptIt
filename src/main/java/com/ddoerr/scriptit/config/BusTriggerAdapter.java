package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.triggers.BusTrigger;
import com.google.gson.*;

import java.lang.reflect.Type;

public class BusTriggerAdapter implements JsonSerializer<BusTrigger>, JsonDeserializer<BusTrigger> {
    @Override
    public BusTrigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String bus = jsonObject.getAsJsonPrimitive("bus").getAsString();
        String id = jsonObject.getAsJsonPrimitive("id").getAsString();

        return new BusTrigger(bus, id);
    }

    @Override
    public JsonElement serialize(BusTrigger src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("bud", src.getBusName());
        obj.addProperty("id", src.getId());

        return obj;
    }
}
