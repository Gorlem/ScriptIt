package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.hud.HudHorizontalAnchor;
import com.google.gson.*;
import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementFactory;
import com.ddoerr.scriptit.api.hud.HudVerticalAnchor;
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

        json.addProperty("type", src.getClass().getSimpleName());
        json.add("relative", context.serialize(src.getRelativePosition()));
        json.add("options",  context.serialize(src.getOptions()));

        return json;
    }

    @Override
    public HudElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String type = jsonObject.getAsJsonPrimitive("type").getAsString();
        Point point = context.deserialize(jsonObject.get("relative"), Point.class);

        Map<String, Object> options = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : jsonObject.getAsJsonObject("options").entrySet()) {
            String value = entry.getValue().getAsString();

            HudVerticalAnchor verticalAnchor = EnumUtils.getEnum(HudVerticalAnchor.class, value);
            HudHorizontalAnchor horizontalAnchor = EnumUtils.getEnum(HudHorizontalAnchor.class, value);

            Object result = verticalAnchor != null
                    ? verticalAnchor
                    : horizontalAnchor != null
                        ? horizontalAnchor
                        : value;
            options.put(entry.getKey(), result);
        }

        HudElementFactory factory = hudElementLoader.findByName(type);
        HudElement hudElement = factory.create(0, 0);
        hudElement.setRelativePosition(point);

        for (Map.Entry<String, Object> entry : options.entrySet()) {
            hudElement.setOption(entry.getKey(), entry.getValue());
        }

        return hudElement;
    }
}
