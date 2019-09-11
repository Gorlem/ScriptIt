package com.ddoerr.scriptit.api.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

import java.util.function.Function;

public enum HudHorizontalAnchor implements HudAnchor {
    LEFT((window) -> 0),
    CENTER((window) -> window.getScaledWidth() / 2),
    RIGHT(Window::getScaledWidth);

    private Function<Window, Integer> baseValueFunction;
    private Window window;

    HudHorizontalAnchor(Function<Window, Integer> baseValueFunction) {
        this.baseValueFunction = baseValueFunction;
        window = MinecraftClient.getInstance().window;
    }

    private Window getWindow() {
        if (window == null)
            window = MinecraftClient.getInstance().window;

        return window;
    }

    public int getBaseValue() {
        return baseValueFunction.apply(getWindow());
    }
}
