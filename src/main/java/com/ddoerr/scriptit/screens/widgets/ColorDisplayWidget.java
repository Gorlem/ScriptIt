package com.ddoerr.scriptit.screens.widgets;

import com.ddoerr.scriptit.api.util.Color;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;

public class ColorDisplayWidget extends DrawableHelper implements Drawable, Element {
    int x;
    int y;
    int width;
    int height;

    Color color;

    public ColorDisplayWidget(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public void render(int i, int i1, float v) {
        if (color == null)
            return;

        fill(x - 1, y - 1, x + width + 1, y + height + 1, Color.GREY.getValue());
        fill(x, y, x + width, y + height, color.getValue());
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
