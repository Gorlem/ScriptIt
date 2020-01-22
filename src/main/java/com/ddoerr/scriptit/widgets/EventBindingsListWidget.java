package com.ddoerr.scriptit.widgets;

import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.scripts.Scripts;
import com.google.common.collect.ImmutableList;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.dependencies.Resolver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;

import java.util.List;

public class EventBindingsListWidget extends ElementListWidget<EventBindingsListWidget.EventEntry> {

    public EventBindingsListWidget(MinecraftClient minecraft, int width, int height, int top, int bottom, int entryHeight) {
        super(minecraft, width, height, top, bottom, entryHeight);

        for (ScriptContainer entry : Resolver.getInstance().resolve(Scripts.class).getAll(Scripts.EVENT_CATEGORY)) {
            addEntry(entry);
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

    private void addEntry(ScriptContainer scriptContainer) {
        addEntry(new EventEntry(this, scriptContainer));
    }

    @Override
    protected void renderHoleBackground(int int_1, int int_2, int int_3, int int_4) {}

    public class EventEntry extends ElementListWidget.Entry<EventEntry> {
        ScriptContainer event;
        TextFieldWidget textFieldWidget;
        EventBindingsListWidget parent;

        public EventEntry(EventBindingsListWidget parent, ScriptContainer scriptContainer)  {
            this.event = scriptContainer;
            this.parent = parent;
            MinecraftClient minecraft = MinecraftClient.getInstance();

            String eventTranslation = I18n.translate("scriptit:event."/* + event.getName()*/);
            textFieldWidget = new TextFieldWidget(minecraft.textRenderer, 0,0, 100, 20, eventTranslation);
            textFieldWidget.setMaxLength(1000);
            textFieldWidget.setText(event.getContent());
            textFieldWidget.method_1883(0);
            textFieldWidget.setChangedListener((text) -> {
                event.setContent(text);
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
