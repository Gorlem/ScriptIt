package com.ddoerr.scriptit.widgets;

import net.minecraft.text.LiteralText;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.*;
import spinnery.widget.api.WLayoutElement;

import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

public class ValuesDropdownWidget<T> extends WDropdown {
    T selectedValue;
    Consumer<T> onChange;
    DropdownDirection direction = DropdownDirection.Down;

    public void addValues(T... values) {
        for (T value : values) {
            addChild(value);
        }
    }

    public void addValues(Iterable<T> values) {
        for (T value : values) {
            addChild(value);
        }
    }

    private void addChild(T value) {
        createChild(WStaticText.class)
                .setText(value.toString())
                .setOnMouseClicked((widget, mouseX, mouseY, delta) -> selectValue(value));
    }

    public void selectValue(T value) {
        selectedValue = value;

        setState(false);
        setLabel(new LiteralText(value.toString()));

        if (onChange != null) {
            onChange.accept(value);
        }
    }

    public void setOnChange(Consumer<T> onChange) {
        this.onChange = onChange;
    }

    public T getSelectedValue() {
        return selectedValue;
    }

    public void setDirection(DropdownDirection direction) {
        this.direction = direction;
    }

    @Override
    public int getY() {
        if (direction == DropdownDirection.Down || !getState()) {
            return super.getY();
        }

        return super.getY() - dropdownSize.getHeight() + getHeight();
    }

    @Override
    public void draw() {
        if (!this.isHidden()) {
            int x = this.getX();
            int y = this.getY();
            int z = this.getZ();
            int sX = this.getWidth();
            int sY = this.getHeight();
            BaseRenderer.drawPanel((double)x, (double)y, (double)z, (double)sX, (double)sY + 1.75D, this.getStyle().asColor("shadow"), this.getStyle().asColor("background"), this.getStyle().asColor("highlight"), this.getStyle().asColor("outline"));
            if (this.hasLabel()) {
                TextRenderer.pass().shadow(this.isLabelShadowed()).text(this.getLabel()).at(x + sX / 2 - TextRenderer.width(this.getLabel()) / 2, y + 6, z).color(this.getStyle().asColor("label.color")).shadowColor(this.getStyle().asColor("label.shadow_color")).render();
                if (this.getState()) {
                    BaseRenderer.drawRectangle((double)x, (double)(y + 16), (double)z, (double)sX, 1.0D, this.getStyle().asColor("outline"));
                    BaseRenderer.drawRectangle((double)(x + 1), (double)(y + 17), (double)z, (double)(sX - 2), 0.75D, this.getStyle().asColor("shadow"));
                }
            }

            if (this.getState()) {
                Iterator var6 = this.getOrderedWidgets().iterator();

                while(var6.hasNext()) {
                    WLayoutElement widgetC = (WLayoutElement)var6.next();
                    widgetC.draw();
                }
            }

        }
    }

    public enum DropdownDirection {
        Up,
        Down
    }
}
