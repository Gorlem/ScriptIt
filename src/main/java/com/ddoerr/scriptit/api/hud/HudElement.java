package com.ddoerr.scriptit.api.hud;

import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;

public interface HudElement {
    Rectangle render(Point origin, HudElementContainer hudElement);
    void setDefaults(HudElementContainer hudElement);
}
