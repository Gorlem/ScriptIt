package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.LifeCycle;
import com.ddoerr.scriptit.ScriptContainer;
import com.ddoerr.scriptit.triggers.Trigger;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ScriptContainerAdapter implements JsonSerializer<ScriptContainer>, JsonDeserializer<ScriptContainer> {
    @Override
    public JsonElement serialize(ScriptContainer scriptContainer, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("content", scriptContainer.getContent());
        obj.add("lifeCycle", context.serialize(scriptContainer.getLifeCycle()));
        obj.add("trigger", context.serialize(scriptContainer.getTrigger()));

        return obj;
    }

    @Override
    public ScriptContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String content = jsonObject.getAsJsonPrimitive("content").getAsString();
        LifeCycle lifeCycle = context.deserialize(jsonObject.get("lifeCycle"), LifeCycle.class);
        Trigger trigger = context.deserialize(jsonObject.get("trigger"), Trigger.class);

        ScriptContainer scriptContainer = new ScriptContainer(trigger, lifeCycle, content);

        return scriptContainer;
    }
}
