package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.widgets.EventBindingsListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.LiteralText;

public class EventsScreen extends Screen {
    EventBindingsListWidget eventBindingsListWidget;

    protected EventsScreen() {
        super(new LiteralText("Event Screen"));
    }

    @Override
    protected void init() {
        super.init();
        minecraft.keyboard.enableRepeatEvents(true);

        Window window = MinecraftClient.getInstance().window;
        eventBindingsListWidget = new EventBindingsListWidget(MinecraftClient.getInstance(), window.getScaledWidth(), window.getScaledHeight(), 0, window.getScaledHeight(), 35);
        children.add(eventBindingsListWidget);
    }

    @Override
    public void removed() {
        super.removed();

        minecraft.keyboard.enableRepeatEvents(false);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);

        eventBindingsListWidget.render(mouseX, mouseY, delta);
    }
}
