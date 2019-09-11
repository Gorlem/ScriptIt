package com.ddoerr.scriptit.api.hud;

import com.ddoerr.scriptit.api.util.geometry.Point;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;

import java.util.Map;

public interface HudElement extends Element, Drawable {
    String BINDING = "binding";
    String FORE_COLOR = "fore-color";
    String BACK_COLOR = "back-color";
    String HORIZONTAL_ANCHOR = "horizontal-anchor";
    String VERTICAL_ANCHOR = "vertical-anchor";

    int getWidth();
    int getHeight();

    void setRealPosition(Point position);
    Point getRealPosition();

    void setRelativePosition(Point position);
    Point getRelativePosition();

    void setOption(String key, Object value);
    <T> T getOption(String key);
    Map<String, Object> getOptions();
}
