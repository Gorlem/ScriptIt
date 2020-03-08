package com.ddoerr.scriptit.util.buttons;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultButtonProvider implements ButtonProvider {
    @Override
    public int getAmount(Screen screen) {
        return getButtons(screen).size();
    }

    @Override
    public void click(Screen screen, int index) {
        List<AbstractPressableButtonWidget> buttons = getButtons(screen);

        if (index < 0 || index >= buttons.size()) {
            return;
        }

        MinecraftClient.getInstance().submit(() -> {
            buttons.get(index).onPress();
        });
    }

    @Override
    public boolean matches(Screen screen) {
        return screen != null;
    }

    @Override
    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        List<AbstractPressableButtonWidget> buttons = getButtons(screen);

        for (int i = 0; i < buttons.size(); i++) {
            AbstractPressableButtonWidget button = buttons.get(i);
            if (button.visible && button.isHovered()) {

                if (screen instanceof BeaconScreen) {
                    mouseY -= 18;
                }
                screen.renderTooltip("Button " + i, mouseX, mouseY);
            }
        }
    }

    private List<AbstractPressableButtonWidget> getButtons(Screen screen) {
        return screen.children()
                .stream()
                .filter(AbstractPressableButtonWidget.class::isInstance)
                .map(AbstractPressableButtonWidget.class::cast)
                .collect(Collectors.toList());
    }
}
