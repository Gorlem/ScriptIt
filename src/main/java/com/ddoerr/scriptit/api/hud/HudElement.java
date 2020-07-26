package com.ddoerr.scriptit.api.hud;

import com.ddoerr.scriptit.api.Identifiable;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import com.ddoerr.scriptit.fields.Field;

import java.util.Map;

public interface HudElement extends Identifiable {
    Rectangle render(Point origin, HudElementContainer hudElement);
    Script getDefaultScript();

    Map<String, Field<?>> getFields();
}
