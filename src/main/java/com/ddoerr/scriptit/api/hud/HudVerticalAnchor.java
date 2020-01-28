package com.ddoerr.scriptit.api.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

import java.util.function.Function;

public enum HudVerticalAnchor implements HudAnchor {
    TOP((window) -> 0),
    MIDDLE((window) -> window.getScaledHeight() / 2),
    BOTTOM(Window::getScaledHeight);

    private Function<Window, Integer> baseValueFunction;
    private Window window;

    HudVerticalAnchor(Function<Window, Integer> baseValueFunction) {
        this.baseValueFunction = baseValueFunction;
        window = MinecraftClient.getInstance().getWindow();
    }

    private Window getWindow() {
        if (window == null)
            window = MinecraftClient.getInstance().getWindow();

        return window;
    }

    public int getBaseValue() {
        return baseValueFunction.apply(getWindow());
    }
}
