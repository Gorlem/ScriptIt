package com.ddoerr.scriptit.util;

import com.ddoerr.scriptit.util.slots.SlotProvider;
import net.minecraft.client.gui.screen.Screen;
import spinnery.client.BaseScreen;
import spinnery.widget.WAbstractWidget;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSpinneryClickablesProvider<W extends WAbstractWidget> implements ClickablesProvider {
    private Class<W> type;
    private String clickablesType;

    public AbstractSpinneryClickablesProvider(Class<W> type) {
        this.type = type;
        clickablesType = this instanceof SlotProvider ? "Slot" : "Button";
    }

    protected List<W> getWidgets(BaseScreen screen) {
        return screen.getInterface().getAllWidgets()
                .stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    @Override
    public int getAmount(Screen screen) {
        return getWidgets((BaseScreen)screen).size();
    }

    @Override
    public boolean matches(Screen screen) {
        return screen instanceof BaseScreen;
    }

    @Override
    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        List<W> widgets = getWidgets((BaseScreen)screen);

        for (int i = 0; i < widgets.size(); i++) {
            W widget = widgets.get(i);

            if (widget.isWithinBounds(mouseX, mouseY)) {
                screen.renderTooltip(clickablesType + " " + i, mouseX, mouseY);
            }
        }
    }
}
