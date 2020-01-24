package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.hud.*;
import com.ddoerr.scriptit.elements.HudElement;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.google.gson.*;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.loader.HudElementLoader;
import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HudElementAdapter implements JsonSerializer<HudElement>, JsonDeserializer<HudElement> {
    private HudElementLoader hudElementLoader;

    public HudElementAdapter() {
        hudElementLoader = Resolver.getInstance().resolve(HudElementLoader.class);
    }

    @Override
    public JsonElement serialize(HudElement src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();

        JsonObject anchor = new JsonObject();
        anchor.add("horizontal", context.serialize(src.getHorizontalAnchor()));
        anchor.add("vertical", context.serialize(src.getVerticalAnchor()));

        json.addProperty("type", hudElementLoader.getName(src.getProvider()));
        json.add("relative", context.serialize(src.getRelativePosition()));
        json.add("anchor", anchor);
        json.add("options",  context.serialize(src.getOptions()));
        json.add("script", context.serialize(src.getScriptContainer()));

        return json;
    }

    @Override
    public HudElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String type = jsonObject.getAsJsonPrimitive("type").getAsString();
        Point point = context.deserialize(jsonObject.get("relative"), Point.class);
        ScriptContainer scriptContainer = context.deserialize(jsonObject.get("script"), ScriptContainer.class);

        JsonObject anchor = jsonObject.getAsJsonObject("anchor");
        HudHorizontalAnchor horizontalAnchor = context.deserialize(anchor.get("horizontal"), HudHorizontalAnchor.class);
        HudVerticalAnchor verticalAnchor = context.deserialize(anchor.get("vertical"), HudVerticalAnchor.class);

        Map<String, Object> options = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : jsonObject.getAsJsonObject("options").entrySet()) {
            String value = entry.getValue().getAsString();
            options.put(entry.getKey(), value);
        }

        HudElementProvider factory = hudElementLoader.findByName(type);
        HudElement hudElement = new HudElement(factory, 0, 0);
        hudElement.setRelativePosition(point);

        for (Map.Entry<String, Object> entry : options.entrySet()) {
            hudElement.setOption(entry.getKey(), entry.getValue());
        }

        hudElement.getScriptContainer().setContent(scriptContainer.getContent());
        hudElement.setAnchor(horizontalAnchor, verticalAnchor);

        return hudElement;
    }
}
