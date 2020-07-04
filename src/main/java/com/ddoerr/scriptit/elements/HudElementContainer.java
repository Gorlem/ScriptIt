package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudHorizontalAnchor;
import com.ddoerr.scriptit.api.hud.HudVerticalAnchor;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.triggers.ContinuousTrigger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.util.Tickable;

import java.util.HashMap;
import java.util.Map;

public interface HudElementContainer extends Drawable, Tickable  {
    public static final int DEFAULT_PADDING = 2;

    public static final String FORE_COLOR = "fore-color";
    public static final String BACK_COLOR = "back-color";

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

    static Color parseAndRun(String value) {
        Color color = Color.parse(value);

        if (color != null)
            return color;

        try {
            String result = new ScriptBuilder().fromString(value).run();
            return Color.parse(result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
