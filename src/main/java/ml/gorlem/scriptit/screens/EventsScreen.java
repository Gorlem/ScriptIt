package ml.gorlem.scriptit.screens;

import ml.gorlem.scriptit.api.util.Color;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.scripts.ScriptBinding;
import ml.gorlem.scriptit.scripts.ScriptBindings;
import ml.gorlem.scriptit.widgets.EventBindingsListWidget;
import ml.gorlem.scriptit.widgets.KeyBindingsListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Window;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class EventsScreen extends Screen {
    EventBindingsListWidget eventBindingsListWidget;

    protected EventsScreen() {
        super(new LiteralText("Event Screen"));
    }

    @Override
    protected void init() {
        super.init();

        Window window = MinecraftClient.getInstance().window;
        eventBindingsListWidget = new EventBindingsListWidget(MinecraftClient.getInstance(), window.getScaledWidth(), window.getScaledHeight(), 0, window.getScaledHeight(), 35);
        children.add(eventBindingsListWidget);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);

        eventBindingsListWidget.render(mouseX, mouseY, delta);
    }
}
