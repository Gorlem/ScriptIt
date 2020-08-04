package com.ddoerr.scriptit.extension.elements;

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
import com.ddoerr.scriptit.elements.AbstractHudElement;
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
import java.util.stream.Stream;

public class TextHudElement extends AbstractHudElement {
    public static final Identifier IDENTIFIER = new Identifier(ScriptItMod.MOD_NAME, "text");
    public static final String NEWLINE_REGEX = "\\R";

    public static final String BACK_COLOR_FIELD = "back-color";
    public static final String FORE_COLOR_FIELD = "fore-color";

    private ColorField backColorField;
    private ColorField foreColorField;

    private MinecraftClient minecraft;

    public TextHudElement(MinecraftClient minecraft) {
        this.minecraft = minecraft;

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
        String lastResult = StringUtils.EMPTY;
        try {
            ContainedValue containedValue = hudElement.getScriptContainer().getLastResult();
            if (containedValue != null) {
                lastResult = containedValue.toStr().trim();
            }
        } catch (ConversionException e) {
            e.printStackTrace();
        }

        String[] lines = lastResult.split(NEWLINE_REGEX, -1);

        int fontHeight = minecraft.textRenderer.fontHeight;
        int stringHeight = fontHeight * lines.length;
        int stringWidth = Stream.of(lines).map(line -> minecraft.textRenderer.getStringWidth(line)).max(Integer::compareTo).orElse(0);

        Rectangle rectangle = new Rectangle(
                (int) origin.getX(), (int) origin.getY(),
                stringWidth + 2 * HudElementContainer.DEFAULT_PADDING,
                stringHeight + 2 * HudElementContainer.DEFAULT_PADDING);

        Color backColor = Color.runAndParse(backColorField.getValue());
        if (backColor != null) {
            DrawableHelper.fill(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(), backColor.getValue());
        }

        Color foreColor = Color.runAndParse(foreColorField.getValue());
        if (foreColor != null) {
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                int x = rectangle.getMinX() + HudElementContainer.DEFAULT_PADDING;
                int y = rectangle.getMinY() + HudElementContainer.DEFAULT_PADDING + (i * fontHeight);
                minecraft.textRenderer.drawWithShadow(line, x, y, foreColor.getValue());
            }
        }
        return rectangle;
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Script getDefaultScript() {
        return new ScriptBuilder().fromString("Text Hud Element");
    }
}
