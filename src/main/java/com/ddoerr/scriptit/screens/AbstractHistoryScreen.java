package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.dependencies.Resolver;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseScreen;

import java.util.function.Supplier;

public class AbstractHistoryScreen extends BaseScreen {
    private ScreenHistory history;

    public AbstractHistoryScreen() {
        history = Resolver.getInstance().resolve(ScreenHistory.class);
    }

    @Override
    public void onClose() {
        history.back();
    }

    protected void openScreen(Supplier<Screen> screenSupplier) {
        history.open(screenSupplier);
    }

    @Override
    public boolean keyPressed(int keyCode, int character, int keyModifier) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            onClose();
            return true;
        } else {
            super.keyPressed(keyCode, character, keyModifier);
            return false;
        }
    }
}
