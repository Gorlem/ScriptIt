package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.triggers.BusTrigger;
import com.ddoerr.scriptit.triggers.DurationTrigger;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.triggers.TriggerFactory;
import com.google.gson.*;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TriggerAdapter implements JsonDeserializer<Trigger>, JsonSerializer<Trigger> {
    private TriggerFactory triggerFactory;

    public TriggerAdapter(TriggerFactory triggerFactory) {
        this.triggerFactory = triggerFactory;
    }

    @Override
    public Trigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String type = jsonObject.getAsJsonPrimitive("type").getAsString();
        Identifier identifier = new Identifier(type);

        Map<String, String> data = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.getAsJsonObject("data").entrySet()) {
            String value = entry.getValue().getAsString();
            data.put(entry.getKey(), value);
        }

        return triggerFactory.createTrigger(identifier, data);
    }

    @Override
    public JsonElement serialize(Trigger src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("type", src.getIdentifier().toString());
        obj.add("data", context.serialize(src.getData()));

        return obj;
    }
}
