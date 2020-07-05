package com.ddoerr.scriptit.api.hud;

import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudHorizontalAnchor;
import com.ddoerr.scriptit.api.hud.HudVerticalAnchor;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.util.Tickable;

import java.util.Map;

public interface HudElementContainer extends Drawable, Tickable  {
    int DEFAULT_PADDING = 2;

    String FORE_COLOR = "fore-color";
    String BACK_COLOR = "back-color";

    void setAnchor(HudHorizontalAnchor horizontalAnchor, HudVerticalAnchor verticalAnchor);
    HudVerticalAnchor getVerticalAnchor();
    HudHorizontalAnchor getHorizontalAnchor();

    void setRealPosition(Point position);
    Point getRealPosition();

    void setRelativePosition(Point position);
    Point getRelativePosition();

    int getHeight();
    int getWidth();

    void setOption(String key, Object value);
    Object getOption(String key);
    Map<String, Object> getOptions();

    HudElement getHudElement();
    ScriptContainer getScriptContainer();
}
