package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.hud.*;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.elements.HudElementContainerImpl;
import com.ddoerr.scriptit.fields.Field;
import com.google.gson.*;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HudElementAdapter implements JsonSerializer<HudElementContainer>, JsonDeserializer<HudElementContainer> {
    private ScriptItRegistry registry;

    public HudElementAdapter(ScriptItRegistry registry) {
        this.registry = registry;
    }

    @Override
    public JsonElement serialize(HudElementContainer src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();

        JsonObject anchor = new JsonObject();
        anchor.add("horizontal", context.serialize(src.getHorizontalAnchor()));
        anchor.add("vertical", context.serialize(src.getVerticalAnchor()));

        Map<String, Field<?>> fieldValues = src.getHudElement().getFields();
        Map<String, String> data = new HashMap<>();

        for (Map.Entry<String, Field<?>> entry : fieldValues.entrySet()) {
            data.put(entry.getKey(), entry.getValue().serialize());
        }

        json.addProperty("type", src.getHudElement().getIdentifier().toString());
        json.add("relative", context.serialize(src.getRelativePosition()));
        json.add("anchor", anchor);
        json.add("data", context.serialize(data));
        json.add("script", context.serialize(src.getScriptContainer(), ScriptContainer.class));

        return json;
    }

    @Override
    public HudElementContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String type = jsonObject.getAsJsonPrimitive("type").getAsString();
        Point point = context.deserialize(jsonObject.get("relative"), Point.class);
        ScriptContainer scriptContainer = context.deserialize(jsonObject.get("script"), ScriptContainer.class);

        JsonObject anchor = jsonObject.getAsJsonObject("anchor");
        HudHorizontalAnchor horizontalAnchor = context.deserialize(anchor.get("horizontal"), HudHorizontalAnchor.class);
        HudVerticalAnchor verticalAnchor = context.deserialize(anchor.get("vertical"), HudVerticalAnchor.class);

        Map<String, String> data = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : jsonObject.getAsJsonObject("data").entrySet()) {
            String value = entry.getValue().getAsString();
            data.put(entry.getKey(), value);
        }

        HudElementFactory hudElementFactory = registry.hudElements.get(new Identifier(type));
        HudElement hudElement = hudElementFactory.createHudElement(data);
        HudElementContainer hudElementContainer = new HudElementContainerImpl(hudElement, 0, 0);
        hudElementContainer.setRelativePosition(point);

        hudElementContainer.getScriptContainer().setScript(scriptContainer.getScript());
        hudElementContainer.setAnchor(horizontalAnchor, verticalAnchor);

        return hudElementContainer;
    }
}
