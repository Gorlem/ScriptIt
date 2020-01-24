package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.hud.HudElementInitializer;
import com.ddoerr.scriptit.api.hud.HudElementProvider;
import com.ddoerr.scriptit.api.hud.HudElementRegistry;
import com.ddoerr.scriptit.api.util.geometry.Rectangle;
import com.ddoerr.scriptit.screens.Popup;
import com.ddoerr.scriptit.screens.WidgetOptionsPopup;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.api.util.geometry.Point;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.client.gui.DrawableHelper.fill;

public class IconHudElement implements HudElementInitializer, HudElementProvider {

    @Override
    public void onInitialize(HudElementRegistry registry) {
        registry.registerHudElement("Icon", this);
    }

    @Override
    public Rectangle render(Point origin, HudElement hudElement) {
        Rectangle rectangle = new Rectangle((int) origin.getX(), (int) origin.getY(), 20, 20);

        // TODO: parse color also as a script
        Color backColor = Color.parse(hudElement.getOption("back-color").toString());
        if (backColor != null) {
            fill(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(), backColor.getValue());
        }

        try {
            Item item = Registry.ITEM.get(new Identifier(hudElement.getOption("result").toString()));
            GuiLighting.enableForItems();
            MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(item.getStackForRender(), rectangle.getMinX() + 2, rectangle.getMinY() + 2);
        }
        catch (Exception e) { }

        return rectangle;
    }

    @Override
    public Map<String, Object> defaultOptions() {
        HashMap<String, Object> options = new HashMap<>();

        options.put("binding", "return \"grass_block\"");

        return options;
    }
}
