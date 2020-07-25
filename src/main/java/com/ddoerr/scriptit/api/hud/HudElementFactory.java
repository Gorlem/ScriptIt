package com.ddoerr.scriptit.api.hud;

import com.ddoerr.scriptit.fields.Field;

import java.util.Map;

public interface HudElementFactory {
    HudElement createHudElement();
    default HudElement createHudElement(Map<String, String> data) {
        HudElement hudElement = createHudElement();

        for (Map.Entry<String, Field<?>> entry : hudElement.getFields().entrySet()) {
            if (data.containsKey(entry.getKey())) {
                entry.getValue().deserialize(data.get(entry.getKey()));
            }
        }

        return hudElement;
    }
}
