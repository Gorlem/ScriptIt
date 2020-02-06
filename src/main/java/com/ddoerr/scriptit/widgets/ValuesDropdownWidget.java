package com.ddoerr.scriptit.widgets;

import net.minecraft.text.LiteralText;
import spinnery.client.BaseRenderer;
import spinnery.widget.*;

import java.util.*;
import java.util.function.Consumer;

public class ValuesDropdownWidget<T> extends WDropdown {
    T selectedValue;
    Consumer<T> onChange;
    DropdownDirection direction = DropdownDirection.Down;

    public ValuesDropdownWidget(WPosition position, WSize size, WInterface linkedInterface) {
        super(position, WSize.of(size.getX(), size.getY(), size.getX(), 20), linkedInterface);
    }

    public static WWidget.Theme of(Map<String, String> rawTheme) {
        return WDropdown.of(rawTheme);
    }

    public void addValues(T... values) {
        for (T value : values) {
            addChild(value);
        }

        size.setY(1, 20 + dropdownWidgets.size() * 11);
    }

    public void addValues(Iterable<T> values) {
        for (T value : values) {
            addChild(value);
        }

        size.setY(1, 20 + dropdownWidgets.size() * 11);
    }

    private void addChild(T value) {
        WStaticText textRow = new WStaticText(
                WPosition.of(WType.ANCHORED, 0, 0, 1, this),
                getInterface(),
                new LiteralText(value.toString()));
        textRow.setOnMouseClicked(() -> selectValue(value));

        add(textRow);
    }

    public void selectValue(T value) {
        selectedValue = value;

        setLabel(new LiteralText(value.toString()));
        setState(false);

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

        return super.getY() - getHeight(1) + getHeight(0);
    }

    @Override
    public void draw() {
        if (isHidden()) {
            return;
        }

        int x = getX();
        int y = getY();
        int z = getZ();

        int sX = getWidth();
        int sY = getHeight();

        int difference = direction == DropdownDirection.Down || !getState() ? 0 : getHeight(1) - getHeight(0);

        BaseRenderer.drawPanel(getX(), getY(), getZ(), getWidth(), getHeight() + 1.75, getResourceAsColor(SHADOW), getResourceAsColor(BACKGROUND), getResourceAsColor(HIGHLIGHT), getResourceAsColor(OUTLINE));

        if (hasLabel()) {
            BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), x + sX / 2 - BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) / 2, y + 6 + difference, getResourceAsColor(LABEL).RGB);

            if (difference != 0) {
                difference -= 13;
            }
            if (getState()) {
                BaseRenderer.drawRectangle(x, y + 16 + difference, z, sX, 1, getResourceAsColor(OUTLINE));
                BaseRenderer.drawRectangle(x + 1, y + 17 + difference, z, sX - 2, 0.75, getResourceAsColor(SHADOW));
            }
        }

        if (getState()) {
            for (List<WWidget> widgetB : getDropdownWidgets()) {
                for (WWidget widgetC : widgetB) {
                    widgetC.draw();
                }
            }
        }
    }

    @Override
    public void updatePositions() {
        int y = 0;
        if (direction == DropdownDirection.Down) {
            y = getY() + (hasLabel() ? 20 : 8);
        } else {
            y = super.getY() - getHeight(1) + getHeight(0) + 5;
        }

        for (List<WWidget> widgetA : getDropdownWidgets()) {
            int x = getX() + 4;
            for (WWidget widgetB : widgetA) {
                widgetB.setX(x);
                widgetB.setY(y);
                x += widgetB.getWidth() + 2;
            }
            y += widgetA.get(0).getHeight() + 2;
        }
    }

    public enum DropdownDirection {
        Up,
        Down
    }
}
