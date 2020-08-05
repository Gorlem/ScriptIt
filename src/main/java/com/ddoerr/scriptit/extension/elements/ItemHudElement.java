package com.ddoerr.scriptit.extension.elements;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementContainer;
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
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.client.gui.DrawableHelper.fill;

public class ItemHudElement extends AbstractHudElement {
    public static final Identifier IDENTIFIER = new Identifier(ScriptItMod.MOD_NAME, "item");
    public static final int ELEMENT_SIZE = 20;

    public static final String BACK_COLOR_FIELD = "back-color";

    private ColorField backColorField;

    public ItemHudElement() {
        backColorField = new ColorField();
        backColorField.setTitle(new LiteralText("Back Color"));
        backColorField.setDescription(new LiteralText("Color used as the background"));
        backColorField.setValue("BLACK 50%");
        fields.put(BACK_COLOR_FIELD, backColorField);
    }

    @Override
    public Rectangle render(Point origin, HudElementContainer hudElement) {
        Rectangle rectangle = new Rectangle((int) origin.getX(), (int) origin.getY(), ELEMENT_SIZE, ELEMENT_SIZE);

        Color backColor = Color.runAndParse(backColorField.getValue());
        if (backColor != null) {
            fill(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(), backColor.getValue());
        }

        try {
            String lastResult = hudElement.getScriptContainer().getLastResult().toStr();
            Item item = Registry.ITEM.get(new Identifier(lastResult));
            DiffuseLighting.enableGuiDepthLighting();
            MinecraftClient.getInstance()
                    .getItemRenderer()
                    .renderGuiItemIcon(item.getStackForRender(), rectangle.getMinX() + HudElementContainer.DEFAULT_PADDING, rectangle.getMinY() + HudElementContainer.DEFAULT_PADDING);
        }
        catch (Exception ignored) { }

        return rectangle;
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Script getDefaultScript() {
        return new ScriptBuilder().fromString("grass_block");
    }
}
