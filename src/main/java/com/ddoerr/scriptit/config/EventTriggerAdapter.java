package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.triggers.EventTrigger;
import com.google.gson.*;

import java.lang.reflect.Type;

public class EventTriggerAdapter implements JsonSerializer<EventTrigger>, JsonDeserializer<EventTrigger> {
    @Override
    public EventTrigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String id = jsonObject.getAsJsonPrimitive("event").getAsString();

        return new EventTrigger(id);
    }

    @Override
    public JsonElement serialize(EventTrigger src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("event", src.getName());

        return obj;
    }
}
