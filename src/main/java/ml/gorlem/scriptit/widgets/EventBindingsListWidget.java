package ml.gorlem.scriptit.widgets;

import com.google.common.collect.ImmutableList;
import ml.gorlem.scriptit.api.events.Event;
import ml.gorlem.scriptit.api.util.Color;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.events.EventManager;
import ml.gorlem.scriptit.loader.EventLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;

import java.util.List;
import java.util.Map;

public class EventBindingsListWidget extends ElementListWidget<EventBindingsListWidget.EventEntry> {

    public EventBindingsListWidget(MinecraftClient minecraft, int width, int height, int top, int bottom, int entryHeight) {
        super(minecraft, width, height, top, bottom, entryHeight);

        for (Map.Entry<String, Event> entry : Resolver.getInstance().resolve(EventLoader.class).getDispatchers().entrySet()) {
            addEntry(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public int getRowWidth() {
        return width - 15;
    }

    @Override
    protected int getScrollbarPosition() {
        return width - 5;
    }

    private void addEntry(Event entry, String name) {
        addEntry(new EventEntry(this, entry, name));
    }

    @Override
    protected void renderHoleBackground(int int_1, int int_2, int int_3, int int_4) {}

    public class EventEntry extends ElementListWidget.Entry<EventEntry> {
        Event event;
        TextFieldWidget textFieldWidget;
        EventBindingsListWidget parent;

        public EventEntry(EventBindingsListWidget parent, Event event, String name)  {
            this.event = event;
            this.parent = parent;

            EventManager eventManager = Resolver.getInstance().resolve(EventManager.class);

            MinecraftClient minecraft = MinecraftClient.getInstance();

            String eventTranslation = I18n.translate("scriptit:event." + name);
            textFieldWidget = new TextFieldWidget(minecraft.textRenderer, 0,0, 100, 20, eventTranslation);
            textFieldWidget.setMaxLength(1000);
            textFieldWidget.setText(eventManager.getContent(event));
            textFieldWidget.method_1883(0);
            textFieldWidget.setChangedListener((text) -> {
                eventManager.setContent(event, text);
            });
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(textFieldWidget);
        }

        @Override
        public void render(int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float delta) {
            textFieldWidget.x = x + 5;
            textFieldWidget.y = y + 12;
            textFieldWidget.setWidth(width - 10);

            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

            for (Element element : children()) {
                if (element instanceof Drawable) {
                    ((Drawable) element).render(mouseX, mouseY, delta);
                }

                if (element instanceof TextFieldWidget) {
                    AbstractButtonWidget buttonWidget = ((AbstractButtonWidget) element);

                    String key = buttonWidget.getMessage();
                    drawString(textRenderer, key, buttonWidget.x, buttonWidget.y - 10, Color.WHITE.getValue());
                }
            }
        }
    }
}
