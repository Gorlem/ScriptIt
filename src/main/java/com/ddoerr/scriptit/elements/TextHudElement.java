package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.exceptions.ConversionException;
import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementContainer;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import org.apache.commons.lang3.StringUtils;

public class TextHudElement implements HudElement {
    @Override
    public Rectangle render(Point origin, HudElementContainer hudElement) {
        String lastResult = null;
        try {
            lastResult = hudElement.getScriptContainer().getLastResult().toStr();
        } catch (ConversionException e) {
            e.printStackTrace();
        }

        Rectangle rectangle = new Rectangle(
                (int) origin.getX(), (int) origin.getY(),
                MinecraftClient.getInstance().textRenderer.getStringWidth(lastResult == null ? StringUtils.EMPTY : lastResult) + 2 * HudElementContainer.DEFAULT_PADDING,
                MinecraftClient.getInstance().textRenderer.fontHeight + 2 * HudElementContainer.DEFAULT_PADDING);

        Color backColor = Color.runAndParse(hudElement.getOption(HudElementContainer.BACK_COLOR).toString());
        if (backColor != null) {
            DrawableHelper.fill(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(), backColor.getValue());
        }

        Color foreColor = Color.runAndParse(hudElement.getOption(HudElementContainer.FORE_COLOR).toString());
        if (foreColor != null) {
            MinecraftClient.getInstance().textRenderer
                    .drawWithShadow(lastResult == null ? StringUtils.EMPTY : lastResult, rectangle.getMinX() + HudElementContainer.DEFAULT_PADDING, rectangle.getMinY() + HudElementContainer.DEFAULT_PADDING, foreColor.getValue());
        }

        return rectangle;
    }

    @Override
    public void setDefaults(HudElementContainer hudElement) {
        hudElement.getScriptContainer().setScript(new ScriptBuilder().fromString("return \"Text Hud Element\""));
        hudElement.setOption(HudElementContainer.FORE_COLOR, "WHITE");
        hudElement.setOption(HudElementContainer.BACK_COLOR, "BLACK 50%");
    }
}
