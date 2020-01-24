package com.ddoerr.scriptit.api.hud;

import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;

import java.util.Map;

public interface HudElementProvider {
    Rectangle render(Point origin, HudElement hudElement);
    Map<String, Object> defaultOptions();
}
