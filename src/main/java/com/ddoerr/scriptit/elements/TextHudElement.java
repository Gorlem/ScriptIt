package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.exceptions.ConversionException;
import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementContainer;
import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import com.ddoerr.scriptit.fields.ColorField;
import com.ddoerr.scriptit.fields.Field;
import com.ddoerr.scriptit.fields.StringField;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TextHudElement extends AbstractHudElement {
    public static final Identifier IDENTIFIER = new Identifier(ScriptItMod.MOD_NAME, "text");

    public static final String BACK_COLOR_FIELD = "back-color";
    public static final String FORE_COLOR_FIELD = "fore-color";

    private ColorField backColorField;
    private ColorField foreColorField;

    public TextHudElement() {
        foreColorField = new ColorField();
        foreColorField.setTitle(new LiteralText("Fore Color"));
        foreColorField.setDescription(new LiteralText("Color used for the font"));
        foreColorField.setValue("WHITE");
        fields.put(FORE_COLOR_FIELD, foreColorField);

        backColorField = new ColorField();
        backColorField.setTitle(new LiteralText("Back Color"));
        backColorField.setDescription(new LiteralText("Color used as the background"));
        backColorField.setValue("BLACK 50%");
        fields.put(BACK_COLOR_FIELD, backColorField);
    }

    @Override
    public Rectangle render(Point origin, HudElementContainer hudElement) {
        String lastResult = null;
        try {
            ContainedValue containedValue = hudElement.getScriptContainer().getLastResult();
            if (containedValue != null) {
                lastResult = containedValue.toStr();
            }
        } catch (ConversionException e) {
            e.printStackTrace();
        }

        Rectangle rectangle = new Rectangle(
                (int) origin.getX(), (int) origin.getY(),
                MinecraftClient.getInstance().textRenderer.getStringWidth(lastResult == null ? StringUtils.EMPTY : lastResult) + 2 * HudElementContainer.DEFAULT_PADDING,
                MinecraftClient.getInstance().textRenderer.fontHeight + 2 * HudElementContainer.DEFAULT_PADDING);

        Color backColor = Color.runAndParse(backColorField.getValue());
        if (backColor != null) {
            DrawableHelper.fill(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(), backColor.getValue());
        }

        Color foreColor = Color.runAndParse(foreColorField.getValue());
        if (foreColor != null) {
            MinecraftClient.getInstance().textRenderer
                    .drawWithShadow(lastResult == null ? StringUtils.EMPTY : lastResult, rectangle.getMinX() + HudElementContainer.DEFAULT_PADDING, rectangle.getMinY() + HudElementContainer.DEFAULT_PADDING, foreColor.getValue());
        }

        return rectangle;
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Script getDefaultScript() {
        return new ScriptBuilder().fromString("return \"Text Hud Element\"");
    }
}
