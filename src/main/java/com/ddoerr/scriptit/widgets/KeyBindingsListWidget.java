package com.ddoerr.scriptit.widgets;

import com.ddoerr.scriptit.util.Color;
import com.ddoerr.scriptit.screens.ScriptEditorScreen;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.scripts.Scripts;
import com.google.common.collect.ImmutableList;
import com.ddoerr.scriptit.dependencies.Resolver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.resource.language.I18n;

import java.util.List;

public class KeyBindingsListWidget extends ElementListWidget<KeyBindingsListWidget.KeyBindingEntry> {

    public KeyBindingsListWidget(MinecraftClient minecraft, int width, int height, int top, int bottom, int entryHeight) {
        super(minecraft, width, height, top, bottom, entryHeight);

        for (ScriptContainer scriptBinding : Resolver.getInstance().resolve(Scripts.class).getAll()) {
            addEntry(scriptBinding);
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

    public void addEntry(ScriptContainer scriptBinding) {
        addEntry(new KeyBindingEntry(this, scriptBinding));
    }

    public void removeEntry(ScriptContainer scriptBinding) {
        KeyBindingEntry toBeRemoved = null;

        for (KeyBindingEntry entry : children()) {
            if (entry.scriptBinding.equals(scriptBinding)) {
                toBeRemoved = entry;
                break;
            }
        }

        removeEntry(toBeRemoved);
    }

    @Override
    protected void renderHoleBackground(int int_1, int int_2, int int_3, int int_4) {}

    public class KeyBindingEntry extends ElementListWidget.Entry<KeyBindingsListWidget.KeyBindingEntry> {
        ScriptContainer scriptBinding;

        ButtonWidget removeWidget;
        ButtonWidget saveWidget;

        KeyBindingsListWidget parent;

        public KeyBindingEntry(KeyBindingsListWidget parent, ScriptContainer scriptBinding) {
            this.scriptBinding = scriptBinding;
            this.parent = parent;

            removeWidget = new ButtonWidget(0, 0, 50, 20, I18n.translate("scriptit:bindings.remove"), (button) -> {
                Resolver.getInstance().resolve(Scripts.class).remove(scriptBinding);
                scriptBinding.getTrigger().close();
                parent.removeEntry(scriptBinding);
            });

            saveWidget = new ButtonWidget(0, 0, 50, 20, "Edit", (button) -> {
                minecraft.openScreen(new ScriptEditorScreen(scriptBinding));
            });
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(removeWidget, saveWidget);
        }

        @Override
        public void render(int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float delta) {

            KeyBindingsListWidget.this.drawString(
                    KeyBindingsListWidget.this.minecraft.textRenderer,
                    scriptBinding.toString(),
                    x, y + 7,
                    Color.WHITE.getValue());

            removeWidget.x = x + width - 105;
            removeWidget.y = y + 2;

            saveWidget.x = x + width - 55;
            saveWidget.y = y + 2;

            for (Element element : children()) {
                if (element instanceof Drawable) {
                    ((Drawable) element).render(mouseX, mouseY, delta);
                }
            }
        }
    }
}
