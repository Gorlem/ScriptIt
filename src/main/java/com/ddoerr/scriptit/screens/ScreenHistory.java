package com.ddoerr.scriptit.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.Stack;
import java.util.function.Supplier;

public class ScreenHistory {
    Stack<Supplier<Screen>> screenStack =  new Stack<>();

    MinecraftClient minecraft;

    public ScreenHistory() {
        minecraft = MinecraftClient.getInstance();
    }

    public void open(Supplier<Screen> screenSupplier) {
        push(screenSupplier);
        minecraft.openScreen(screenSupplier.get());
    }

    public void push(Supplier<Screen> screenSupplier) {
        screenStack.push(screenSupplier);
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
