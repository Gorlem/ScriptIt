package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.scripts.ScriptContainerImpl;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ScriptContainerAdapter implements JsonSerializer<ScriptContainer>, JsonDeserializer<ScriptContainer> {
    @Override
    public JsonElement serialize(ScriptContainer scriptContainer, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        Script script = scriptContainer.getScript();
        obj.addProperty("name", script.getName());
//        obj.addProperty("language", script.getLanguage().toString());
        obj.addProperty("content", script.getScriptSource().getContent());
        obj.add("trigger", context.serialize(scriptContainer.getTrigger()));

        return obj;
    }

    @Override
    public ScriptContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String name = jsonObject.getAsJsonPrimitive("name").getAsString();
        String content = jsonObject.getAsJsonPrimitive("content").getAsString();
        Trigger trigger = context.deserialize(jsonObject.get("trigger"), Trigger.class);

        Script script = new ScriptBuilder()
                .name(name)
                .fromString(content);

        return new ScriptContainerImpl(trigger, script);
    }
}
