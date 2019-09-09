package ml.gorlem.scriptit.screens;

import ml.gorlem.scriptit.ScriptItMod;
import ml.gorlem.scriptit.api.util.Color;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.scripts.ScriptBinding;
import ml.gorlem.scriptit.scripts.ScriptBindings;
import ml.gorlem.scriptit.widgets.KeyBindingsListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Window;
import net.minecraft.text.LiteralText;

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
            ScriptBinding scriptBinding = Resolver.getInstance().resolve(ScriptBindings.class).addNew();
            keyBindingsListWidget.addEntry(scriptBinding);
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
