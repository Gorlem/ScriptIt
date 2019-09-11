package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.events.EventBinding;
import com.ddoerr.scriptit.loader.EventLoader;
import com.google.gson.*;
import com.ddoerr.scriptit.dependencies.Resolver;

import java.lang.reflect.Type;

public class EventBindingAdapter implements JsonSerializer<EventBinding>, JsonDeserializer<EventBinding> {
    @Override
    public EventBinding deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject asJsonObject = json.getAsJsonObject();

        String id = asJsonObject.get("id").getAsString();
        String script = asJsonObject.get("script").getAsString();

        EventLoader eventLoader = Resolver.getInstance().resolve(EventLoader.class);
        Event event = eventLoader.findByName(id);

        EventBinding eventBinding = new EventBinding(event);
        eventBinding.setScriptContent(script);
        return eventBinding;
    }

    @Override
    public JsonElement serialize(EventBinding src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("script", src.getScriptContent());

        return jsonObject;
    }
}
