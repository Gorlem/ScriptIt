package com.ddoerr.scriptit.libraries.clickables.buttons;

import com.ddoerr.scriptit.libraries.clickables.AbstractSpinneryClickablesProvider;
import net.minecraft.client.gui.screen.Screen;
import spinnery.client.BaseScreen;
import spinnery.widget.WAbstractButton;

import java.util.List;

public class SpinneryButtonProvider extends AbstractSpinneryClickablesProvider<WAbstractButton> implements ButtonProvider {
    public SpinneryButtonProvider() {
        super(WAbstractButton.class);
    }

    @Override
    public void click(Screen screen, int index) {
        List<WAbstractButton> buttons = getWidgets((BaseScreen)screen);

        if (index < 0 || index >= buttons.size()) {
            return;
        }

        WAbstractButton button = buttons.get(index);
        button.onMouseClicked(button.getX() + 1, button.getY() + 1, 0);
    }
}
