package com.ddoerr.scriptit.widgets;

import spinnery.client.BaseRenderer;
import spinnery.widget.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PanelWidget extends WWidget implements WModifiableCollection, WClient {
    List<WWidget> widgets = new ArrayList<>();
    WColor color;


    public PanelWidget(WPosition position, WSize size, WInterface linkedInterface) {
        setInterface(linkedInterface);
        setPosition(position);
        setSize(size);
        setTheme("light");
    }

    public PanelWidget(WPosition position, WSize size, WInterface linkedInterface, WColor color) {
        setInterface(linkedInterface);
        setPosition(position);
        setSize(size);
        setColor(color);
        setTheme("light");
    }

    public PanelWidget setColor(WColor color) {
        this.color = color;
        return this;
    }

    @Override
    public void draw() {
        super.draw();

        if (isHidden())
            return;

        if (color != null && size != null) {
            BaseRenderer.drawRectangle(getX(), getY(), 0, getWidth(), getHeight(), color);
        }

        for (WWidget widget : getWidgets()) {
            widget.draw();
        }
    }

    @Override
    public void add(WWidget... wWidgets) {
        widgets.addAll(Arrays.asList(wWidgets));
    }

    @Override
    public void remove(WWidget... wWidgets) {
        widgets.removeAll(Arrays.asList(wWidgets));
    }

    @Override
    public List<WWidget> getWidgets() {
        return Collections.unmodifiableList(widgets);
    }

    @Override
    public boolean contains(WWidget... wWidgets) {
        return widgets.containsAll(Arrays.asList(wWidgets));
    }

    @Override
    public boolean updateFocus(int mouseX, int mouseY) {
        this.setFocus(this.isWithinBounds(mouseX, mouseY) && this.getWidgets().stream().noneMatch(WWidget::getFocus));
        return this.getFocus();
    }

    @Override
    public void setX(int x) {
        super.setX(x);

        for (WWidget widget : widgets) {
            widget.align();
        }
    }

    @Override
    public void setY(int y) {
        super.setY(y);

        for (WWidget widget : widgets) {
            widget.align();
        }
    }
}
