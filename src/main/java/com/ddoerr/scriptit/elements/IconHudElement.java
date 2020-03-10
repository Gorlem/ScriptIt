package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElementInitializer;
import com.ddoerr.scriptit.api.hud.HudElementProvider;
import com.ddoerr.scriptit.api.hud.HudElementRegistry;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.api.util.geometry.Point;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.client.gui.DrawableHelper.fill;

public class IconHudElement implements HudElementInitializer, HudElementProvider {
    public static final int ELEMENT_SIZE = 20;


    @Override
    public void onInitialize(HudElementRegistry registry) {
        registry.registerHudElement("Icon", this);
    }

    @Override
    public Rectangle render(Point origin, HudElement hudElement) {
        Rectangle rectangle = new Rectangle((int) origin.getX(), (int) origin.getY(), ELEMENT_SIZE, ELEMENT_SIZE);

        Color backColor = HudElement.parseAndRun(hudElement.getOption(HudElement.BACK_COLOR).toString());
        if (backColor != null) {
            fill(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(), backColor.getValue());
        }

        try {
            String lastResult = hudElement.getScriptContainer().getLastResult().toString();
            Item item = Registry.ITEM.get(new Identifier(lastResult));
            DiffuseLighting.enableGuiDepthLighting();
            MinecraftClient.getInstance()
                    .getItemRenderer()
                    .renderGuiItemIcon(item.getStackForRender(), rectangle.getMinX() + HudElement.DEFAULT_PADDING, rectangle.getMinY() + HudElement.DEFAULT_PADDING);
        }
        catch (Exception ignored) { }

        return rectangle;
    }

    @Override
    public void setDefaults(HudElement hudElement) {
        hudElement.getScriptContainer().setContent("return \"grass_block\"");
        hudElement.setOption(HudElement.BACK_COLOR, "BLACK 50%");
    }
}
