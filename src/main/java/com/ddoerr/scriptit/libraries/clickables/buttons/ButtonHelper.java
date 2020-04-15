package com.ddoerr.scriptit.libraries.clickables.buttons;

import com.ddoerr.scriptit.libraries.clickables.ClickablesHelper;
import net.minecraft.client.gui.screen.Screen;

public class ButtonHelper extends ClickablesHelper<ButtonProvider> {
    public ButtonHelper() {
        provider.add(new EnchantingButtonProvider());
        provider.add(new DefaultButtonProvider());
    }

    public void click(Screen screen, int index) {
        getProvider(screen).ifPresent(p -> p.click(screen, index));
    }
}
