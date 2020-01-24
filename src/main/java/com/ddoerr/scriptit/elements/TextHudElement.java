package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementInitializer;
import com.ddoerr.scriptit.api.hud.HudElementProvider;
import com.ddoerr.scriptit.api.hud.HudElementRegistry;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import com.ddoerr.scriptit.screens.Popup;
import com.ddoerr.scriptit.screens.WidgetOptionsPopup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;

import java.util.HashMap;
import java.util.Map;

public class TextHudElement implements HudElementInitializer, HudElementProvider {
    @Override
    public void onInitialize(HudElementRegistry registry) {
        registry.registerHudElement("Text", this);
    }

    @Override
    public Rectangle render(Point origin, HudElement hudElement) {
        Rectangle rectangle = new Rectangle(
                (int) origin.getX(), (int) origin.getY(),
                MinecraftClient.getInstance().textRenderer.getStringWidth(hudElement.getOption("result")) + 4,
                MinecraftClient.getInstance().textRenderer.fontHeight + 3);

        Color backColor = Color.parse(hudElement.getOption("back-color").toString());
        if (backColor != null) {
            DrawableHelper.fill(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(), backColor.getValue());
        }

        Color foreColor = Color.parse(hudElement.getOption("fore-color").toString());
        if (foreColor != null) {
            MinecraftClient.getInstance().textRenderer
                    .drawWithShadow(hudElement.getOption("result"), rectangle.getMinX() + 2, rectangle.getMinY() + 2, foreColor.getValue());
        }

        return rectangle;
    }

    @Override
    public Map<String, Object> defaultOptions() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("binding", "return \"Text Hud Element\"");
        return map;
    }
}
