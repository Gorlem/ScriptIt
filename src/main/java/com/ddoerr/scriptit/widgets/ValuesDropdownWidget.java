package com.ddoerr.scriptit.widgets;

import net.minecraft.text.LiteralText;
import spinnery.widget.*;

import java.util.*;
import java.util.function.Consumer;

public class ValuesDropdownWidget<T> extends WDropdown {
    List<T> list = new ArrayList<>();
    T selectedValue;
    Consumer<T> onChange;

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
}
