package com.ddoerr.scriptit.widgets;

import com.ddoerr.scriptit.scripts.LifeCycle;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.scripts.Scripts;
import com.ddoerr.scriptit.triggers.BusTrigger;
import com.google.common.collect.ImmutableList;
import com.ddoerr.scriptit.dependencies.Resolver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;

import java.util.List;

public class KeyBindingsListWidget extends ElementListWidget<KeyBindingsListWidget.KeyBindingEntry> {

    public KeyBindingsListWidget(MinecraftClient minecraft, int width, int height, int top, int bottom, int entryHeight) {
        super(minecraft, width, height, top, bottom, entryHeight);

        for (ScriptContainer scriptBinding : Resolver.getInstance().resolve(Scripts.class).getAll(Scripts.KEYBIND_CATEGORY)) {
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

    public static class KeyBindingEntry extends ElementListWidget.Entry<KeyBindingsListWidget.KeyBindingEntry> {
        ScriptContainer scriptBinding;

//        KeyBindingButtonWidget keyBindingButtonWidget;
        TextFieldWidget busFieldWidget;
        TextFieldWidget lifeCycleFieldWidget;
        TextFieldWidget textFieldWidget;
        ButtonWidget removeWidget;
        ButtonWidget saveWidget;

        KeyBindingsListWidget parent;

        public KeyBindingEntry(KeyBindingsListWidget parent, ScriptContainer scriptBinding) {
            this.scriptBinding = scriptBinding;
            this.parent = parent;

            MinecraftClient minecraft = MinecraftClient.getInstance();

//            keyBindingButtonWidget = new KeyBindingButtonWidget( 0, 0, 75, 20, ((KeybindingTrigger)scriptBinding.getTrigger()).getKeyBinding());

            busFieldWidget = new TextFieldWidget(minecraft.textRenderer, 0,0, 100, 20, "");
            busFieldWidget.setMaxLength(1000);
            busFieldWidget.setText(((BusTrigger)scriptBinding.getTrigger()).getId());
            busFieldWidget.method_1883(0);

            lifeCycleFieldWidget = new TextFieldWidget(minecraft.textRenderer, 0,0, 100, 20, "");
            lifeCycleFieldWidget.setMaxLength(1000);
            lifeCycleFieldWidget.setText(scriptBinding.getLifeCycle().toString());
            lifeCycleFieldWidget.method_1883(0);

            textFieldWidget = new TextFieldWidget(minecraft.textRenderer, 0,0, 100, 20, "");
            textFieldWidget.setMaxLength(1000);
            textFieldWidget.setText(scriptBinding.getContent());
            textFieldWidget.method_1883(0);

            removeWidget = new ButtonWidget(0, 0, 50, 20, I18n.translate("scriptit:bindings.remove"), (button) -> {
                Resolver.getInstance().resolve(Scripts.class).remove(Scripts.KEYBIND_CATEGORY, scriptBinding);
                scriptBinding.getTrigger().close();
                parent.removeEntry(scriptBinding);
            });

            saveWidget = new ButtonWidget(0, 0, 50, 20, "Save", (button) -> {
                scriptBinding.setTrigger(new BusTrigger(busFieldWidget.getText()));
                scriptBinding.setLifeCycle(LifeCycle.valueOf(lifeCycleFieldWidget.getText()));
                scriptBinding.setContent(textFieldWidget.getText());
            });
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(busFieldWidget, lifeCycleFieldWidget, textFieldWidget, removeWidget, saveWidget);
        }

        @Override
        public void render(int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float delta) {
//            keyBindingButtonWidget.x = x + 5;
//            keyBindingButtonWidget.y = y + 2;

            busFieldWidget.x = x + 5;
            busFieldWidget.y = y + 2;
            busFieldWidget.setWidth(50);

            lifeCycleFieldWidget.x = x + 60;
            lifeCycleFieldWidget.y = y + 2;
            lifeCycleFieldWidget.setWidth(50);

            textFieldWidget.x = x + 115;
            textFieldWidget.y = y + 2;
            textFieldWidget.setWidth(width - 230);

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
