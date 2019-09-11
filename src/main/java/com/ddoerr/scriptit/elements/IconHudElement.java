package com.ddoerr.scriptit.elements;

import com.ddoerr.scriptit.screens.Popup;
import com.ddoerr.scriptit.screens.WidgetOptionsPopup;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.api.util.geometry.Point;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class IconHudElement extends AbstractHudElement {
    public IconHudElement(double xPosition, double yPosition) {
        super(xPosition, yPosition);
    }

    @Override
    public int getWidth() {
        return 16 + 2 * DEFAULT_PADDING;
    }

    @Override
    public int getHeight() {
        return 16 + 2 * DEFAULT_PADDING;
    }

    @Override
    void initOptions() {
        super.initOptions();

        setOption(BINDING, "return \"grass_block\"");
    }

    @Override
    Popup getOptionsPopup() {
        return new WidgetOptionsPopup(this);
    }

    @Override
    public void render(int var1, int var2, float var3) {
        Point point = getRealPosition();
        double xPosition = point.getX();
        double yPosition = point.getY();

        Color backColor = parseColorFromOption(BACK_COLOR);
        if (backColor != null) {
            fill((int)xPosition, (int)yPosition, (int)xPosition + getWidth(), (int)yPosition + getHeight(), backColor.getValue());
        }

        try {
            Item item = Registry.ITEM.get(new Identifier(lastResult));
            GuiLighting.enableForItems();
            MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(item.getStackForRender(), (int)xPosition + DEFAULT_PADDING, (int)yPosition + DEFAULT_PADDING);
        }
        catch (Exception e) { }

        super.render(var1, var2, var3);
    }
}
