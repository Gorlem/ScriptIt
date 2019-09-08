package ml.gorlem.scriptit.config;

import com.google.gson.*;
import ml.gorlem.scriptit.api.events.Event;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.events.EventBinding;
import ml.gorlem.scriptit.loader.EventLoader;

import java.lang.reflect.Type;

public class EventBindingAdapter implements JsonSerializer<EventBinding>, JsonDeserializer<EventBinding> {
    @Override
    public EventBinding deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject asJsonObject = json.getAsJsonObject();

        String id = asJsonObject.get("id").getAsString();
        String script = asJsonObject.get("script").getAsString();

        EventLoader eventLoader = Resolver.getInstance().resolve(EventLoader.class);
        Event event = eventLoader.findByName(id);

        return new EventBinding(event, script);
    }

    @Override
    public JsonElement serialize(EventBinding src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("script", src.getScriptContent());

        return jsonObject;
    }
}
