package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementContainer;
import com.ddoerr.scriptit.api.hud.HudHorizontalAnchor;
import com.ddoerr.scriptit.api.hud.HudVerticalAnchor;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.elements.HudElementContainerImpl;
import com.google.gson.*;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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

        json.addProperty("type", registry.hudElements.getId(src.getHudElement()).toString());
        json.add("relative", context.serialize(src.getRelativePosition()));
        json.add("anchor", anchor);
        json.add("options", context.serialize(src.getOptions()));
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

        Map<String, Object> options = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : jsonObject.getAsJsonObject("options").entrySet()) {
            String value = entry.getValue().getAsString();
            options.put(entry.getKey(), value);
        }

        HudElement hudElement = registry.hudElements.get(new Identifier(type));
        HudElementContainer hudElementContainer = new HudElementContainerImpl(hudElement, 0, 0);
        hudElementContainer.setRelativePosition(point);

        for (Map.Entry<String, Object> entry : options.entrySet()) {
            hudElementContainer.setOption(entry.getKey(), entry.getValue());
        }

        hudElementContainer.getScriptContainer().setScript(scriptContainer.getScript());
        hudElementContainer.setAnchor(horizontalAnchor, verticalAnchor);

        return hudElementContainer;
    }
}
