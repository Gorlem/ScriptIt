package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementContainer;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.client.gui.DrawableHelper.fill;

public class IconHudElement implements HudElement {
    public static final int ELEMENT_SIZE = 20;

    @Override
    public Rectangle render(Point origin, HudElementContainer hudElement) {
        Rectangle rectangle = new Rectangle((int) origin.getX(), (int) origin.getY(), ELEMENT_SIZE, ELEMENT_SIZE);

        Color backColor = Color.runAndParse(hudElement.getOption(HudElementContainer.BACK_COLOR).toString());
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
    public void setDefaults(HudElementContainer hudElement) {
        hudElement.getScriptContainer().setScript(new ScriptBuilder().fromString("return \"grass_block\""));
        hudElement.setOption(HudElementContainer.BACK_COLOR, "BLACK 50%");
    }
}
