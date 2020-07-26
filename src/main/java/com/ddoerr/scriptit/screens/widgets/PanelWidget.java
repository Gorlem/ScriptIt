package com.ddoerr.scriptit.screens.widgets;

import spinnery.client.BaseRenderer;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.api.Color;
import spinnery.widget.api.WDelegatedEventListener;
import spinnery.widget.api.WEventListener;
import spinnery.widget.api.WModifiableCollection;

import java.util.*;

public class PanelWidget extends WAbstractWidget implements WModifiableCollection, WDelegatedEventListener {
    Set<WAbstractWidget> widgets = new HashSet<>();
    Color color;

    public PanelWidget setColor(Color color) {
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

        for (WAbstractWidget widget : getWidgets()) {
            widget.draw();
        }
    }

    @Override
    public void add(WAbstractWidget... wWidgets) {
        widgets.addAll(Arrays.asList(wWidgets));
    }

    @Override
    public void remove(WAbstractWidget... wWidgets) {
        widgets.removeAll(Arrays.asList(wWidgets));
    }

    @Override
    public Set<WAbstractWidget> getWidgets() {
        return Collections.unmodifiableSet(widgets);
    }

    @Override
    public boolean contains(WAbstractWidget... wWidgets) {
        return widgets.containsAll(Arrays.asList(wWidgets));
    }

    @Override
    public boolean updateFocus(int mouseX, int mouseY) {
        setFocus(isWithinBounds(mouseX, mouseY) && getWidgets().stream().noneMatch((WAbstractWidget::isFocused)));
        return isFocused();
    }

    @Override
    public Collection<? extends WEventListener> getEventDelegates() {
        return widgets;
    }

    @Override
    public <W extends WAbstractWidget> W setHidden(boolean isHidden) {
        for (WAbstractWidget widget : widgets) {
            widget.setHidden(isHidden);
        }

        return super.setHidden(isHidden);
    }
}
