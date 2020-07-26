package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.fields.Field;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractHudElement implements HudElement {
    protected Map<String, Field<?>> fields = new LinkedHashMap<>();

    @Override
    public Map<String, Field<?>> getFields() {
        return fields;
    }
}
