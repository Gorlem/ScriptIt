package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseScreen;

public class AbstractHistoryScreen extends BaseScreen {
    private ScreenHistory history;

    public AbstractHistoryScreen(ScreenHistory history) {
        this.history = history;
    }

    @Override
    public void onClose() {
        history.back();
    }

    protected void openScreen(Class<? extends Screen> screenClass, Object... parameters) {
        history.open(screenClass, parameters);
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
