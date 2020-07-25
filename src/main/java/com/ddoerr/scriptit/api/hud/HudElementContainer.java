package com.ddoerr.scriptit.api.hud;

import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.api.util.geometry.Point;
import net.minecraft.client.gui.Drawable;
import net.minecraft.util.Tickable;

public interface HudElementContainer extends Drawable, Tickable  {
    int DEFAULT_PADDING = 2;

    void setAnchor(HudHorizontalAnchor horizontalAnchor, HudVerticalAnchor verticalAnchor);
    HudVerticalAnchor getVerticalAnchor();
    HudHorizontalAnchor getHorizontalAnchor();

    void setRealPosition(Point position);
    Point getRealPosition();

    void setRelativePosition(Point position);
    Point getRelativePosition();

    int getHeight();
    int getWidth();

    HudElement getHudElement();
    ScriptContainer getScriptContainer();
}
