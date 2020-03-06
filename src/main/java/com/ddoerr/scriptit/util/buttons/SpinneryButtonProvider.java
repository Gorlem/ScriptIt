package com.ddoerr.scriptit.util.buttons;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import spinnery.client.BaseScreen;
import spinnery.widget.WAbstractButton;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SpinneryButtonProvider implements ButtonProvider {
    @Override
    public void click(Screen screen, int index) {
        List<WAbstractButton> buttons = getButtons(screen);

        if (index < 0 || index >= buttons.size()) {
            return;
        }

        WAbstractButton button = buttons.get(index);
        button.onMouseClicked(button.getX() + 1, button.getY() + 1, 0);
    }

    @Override
    public int getAmount(Screen screen) {
        return getButtons(screen).size();
    }

    @Override
    public boolean matches(Screen screen) {
        return screen instanceof BaseScreen;
    }

    @Override
    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        List<WAbstractButton> buttons = getButtons(screen);

        for (int i = 0; i < buttons.size(); i++) {
            WAbstractButton button = buttons.get(i);

            if (button.isWithinBounds(mouseX, mouseY)) {
                screen.renderTooltip("Button " + i, mouseX, mouseY);
            }
        }
    }

    private List<WAbstractButton> getButtons(Screen screen) {
        return ((BaseScreen)screen).getInterface().getAllWidgets()
                .stream()
                .filter(WAbstractButton.class::isInstance)
                .map(WAbstractButton.class::cast)
                .collect(Collectors.toList());
    }
}
