package com.ddoerr.scriptit.extension.libraries.clickables.buttons;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.ducks.TooltipRenderedDuck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

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

                if (((TooltipRenderedDuck) screen).wasTooltipRendered()) {
                    mouseY -= 18;
                }

                screen.renderTooltip(I18n.translate(new Identifier(ScriptItMod.MOD_NAME, "tooltip.button").toString(), i), mouseX, mouseY);
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
