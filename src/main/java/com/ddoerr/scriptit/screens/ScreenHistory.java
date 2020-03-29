package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.Stack;
import java.util.function.Supplier;

public class ScreenHistory {
    private Stack<Supplier<Screen>> screenStack =  new Stack<>();

    private MinecraftClient minecraft;
    private Resolver resolver;

    public ScreenHistory(MinecraftClient minecraft, Resolver resolver) {
        this.minecraft = minecraft;
        this.resolver = resolver;
    }

    private Supplier<Screen> getScreenSupplier(Class<? extends Screen> screenClass, Object... parameters) {
        return () -> {
            try {
                return resolver.create(screenClass, parameters);
            } catch (DependencyException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    public void open(Class<? extends Screen> screenClass, Object... parameters) {
        Supplier<Screen> screenSupplier = getScreenSupplier(screenClass, parameters);

        screenStack.push(screenSupplier);
        minecraft.openScreen(screenSupplier.get());
    }

    public void push(Class<? extends Screen> screenClass, Object... parameters) {
        screenStack.push(getScreenSupplier(screenClass, parameters));
    }

    public void back() {
        pop();
        if (screenStack.isEmpty()) {
            minecraft.openScreen(null);
        } else {
            minecraft.openScreen(screenStack.peek().get());
        }
    }

    public void pop() {
        screenStack.pop();
    }
}
