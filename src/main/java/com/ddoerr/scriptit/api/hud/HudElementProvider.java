package com.ddoerr.scriptit.api.hud;

import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import com.ddoerr.scriptit.elements.HudElement;

public interface HudElementProvider {
    Rectangle render(Point origin, HudElement hudElement);
    void setDefaults(HudElement hudElement);
}
