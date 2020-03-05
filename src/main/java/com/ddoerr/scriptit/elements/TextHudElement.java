package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElementInitializer;
import com.ddoerr.scriptit.api.hud.HudElementProvider;
import com.ddoerr.scriptit.api.hud.HudElementRegistry;
import com.ddoerr.scriptit.util.Color;
import com.ddoerr.scriptit.util.geometry.Point;
import com.ddoerr.scriptit.util.geometry.Rectangle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import org.apache.commons.lang3.StringUtils;

public class TextHudElement implements HudElementInitializer, HudElementProvider {
    @Override
    public void onInitialize(HudElementRegistry registry) {
        registry.registerHudElement("Text", this);
    }

    @Override
    public Rectangle render(Point origin, HudElement hudElement) {
        Object lastResult = hudElement.getScriptContainer().getLastResult();

        Rectangle rectangle = new Rectangle(
                (int) origin.getX(), (int) origin.getY(),
                MinecraftClient.getInstance().textRenderer.getStringWidth(lastResult == null ? StringUtils.EMPTY : lastResult.toString()) + 2 * HudElement.DEFAULT_PADDING,
                MinecraftClient.getInstance().textRenderer.fontHeight + 2 * HudElement.DEFAULT_PADDING);

        Color backColor = HudElement.parseAndRun(hudElement.getOption(HudElement.BACK_COLOR).toString());
        if (backColor != null) {
            DrawableHelper.fill(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(), backColor.getValue());
        }

        Color foreColor = HudElement.parseAndRun(hudElement.getOption(HudElement.FORE_COLOR).toString());
        if (foreColor != null) {
            MinecraftClient.getInstance().textRenderer
                    .drawWithShadow(lastResult == null ? StringUtils.EMPTY : lastResult.toString(), rectangle.getMinX() + HudElement.DEFAULT_PADDING, rectangle.getMinY() + HudElement.DEFAULT_PADDING, foreColor.getValue());
        }

        return rectangle;
    }

    @Override
    public void setDefaults(HudElement hudElement) {
        hudElement.getScriptContainer().setContent("return \"Text Hud Element\"");
        hudElement.setOption(HudElement.FORE_COLOR, "WHITE");
        hudElement.setOption(HudElement.BACK_COLOR, "BLACK 50%");
    }
}
