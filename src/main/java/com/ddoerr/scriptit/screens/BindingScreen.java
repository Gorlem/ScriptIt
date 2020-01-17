package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.ScriptContainer;
import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.Scripts;
import com.ddoerr.scriptit.api.util.KeyBindingHelper;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.triggers.KeybindingTrigger;
import com.ddoerr.scriptit.widgets.KeyBindingsListWidget;
import com.ddoerr.scriptit.api.util.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Window;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class BindingScreen extends Screen {
    public BindingScreen() {
        super(new LiteralText("BindingScreen"));
    }

    private KeyBindingsListWidget keyBindingsListWidget;

    @Override
    protected void init() {
        super.init();
        minecraft.keyboard.enableRepeatEvents(true);

        Window window = MinecraftClient.getInstance().window;
        keyBindingsListWidget = new KeyBindingsListWidget(MinecraftClient.getInstance(), window.getScaledWidth(), window.getScaledHeight() - 50, 0, window.getScaledHeight() - 50, 25);
        children.add(keyBindingsListWidget);

        ButtonWidget addButton = new ButtonWidget(window.getScaledWidth() - 220, window.getScaledHeight() - 35, 200, 20, I18n.translate("scriptit:bindings.add"), (button) -> {
            KeyBinding keyBinding = KeyBindingHelper.create(new Identifier(ScriptItMod.MOD_NAME, UUID.randomUUID().toString()));
            ScriptContainer scriptContainer = new ScriptContainer(new KeybindingTrigger(keyBinding));
            Resolver.getInstance().resolve(Scripts.class).add(Scripts.KEYBIND_CATEGORY, scriptContainer);


            keyBindingsListWidget.addEntry(scriptContainer);
        });
        children.add(addButton);

        ButtonWidget designerButton = new ButtonWidget(20, window.getScaledHeight() - 46, 150, 20, "Designer Screen", (button) -> {
            MinecraftClient.getInstance().openScreen(new WidgetDesignerScreen());
        });
        children.add(designerButton);

        ButtonWidget eventButton = new ButtonWidget(20, window.getScaledHeight() - 24, 150, 20, "Events Screen", (button) -> {
            MinecraftClient.getInstance().openScreen(new EventsScreen());
        });
        children.add(eventButton);
    }

    @Override
    public void removed() {
        super.removed();

        minecraft.keyboard.enableRepeatEvents(false);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);

        Window window = MinecraftClient.getInstance().window;

        keyBindingsListWidget.render(mouseX, mouseY, delta);

        fill(0, window.getScaledHeight() - 50, window.getScaledWidth(), window.getScaledHeight(), Color.BLACK.getValue());

        for (Element element : children) {
            if (element == keyBindingsListWidget) {
                continue;
            }

            if (element instanceof Drawable) {
                ((Drawable) element).render(mouseX, mouseY, delta);
            }
        }
    }
}
