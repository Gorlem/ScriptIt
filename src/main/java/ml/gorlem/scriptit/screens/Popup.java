package ml.gorlem.scriptit.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class Popup extends Screen {
    protected Popup(Text text_1) {
        super(text_1);
    }

    @Override
    public void onClose() {
        Screen screen = MinecraftClient.getInstance().currentScreen;

        if (screen instanceof ScreenWithPopup) {
            ((ScreenWithPopup) screen).clearPopup();
        }
    }
}
