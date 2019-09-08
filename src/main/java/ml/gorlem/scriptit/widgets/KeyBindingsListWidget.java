package ml.gorlem.scriptit.widgets;

import com.google.common.collect.ImmutableList;
import ml.gorlem.scriptit.ScriptItMod;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.scripts.ScriptBinding;
import ml.gorlem.scriptit.scripts.ScriptBindings;
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

        for (ScriptBinding scriptBinding : Resolver.getInstance().resolve(ScriptBindings.class).getAll()) {
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

    public void addEntry(ScriptBinding scriptBinding) {
        addEntry(new KeyBindingEntry(this, scriptBinding));
    }

    public void removeEntry(ScriptBinding scriptBinding) {
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
        ScriptBinding scriptBinding;

        KeyBindingButtonWidget keyBindingButtonWidget;
        TextFieldWidget textFieldWidget;
        ButtonWidget buttonWidget;

        KeyBindingsListWidget parent;

        public KeyBindingEntry(KeyBindingsListWidget parent, ScriptBinding scriptBinding) {
            this.scriptBinding = scriptBinding;
            this.parent = parent;

            MinecraftClient minecraft = MinecraftClient.getInstance();

            keyBindingButtonWidget = new KeyBindingButtonWidget( 0, 0, 75, 20, scriptBinding.getKeyBinding());

            textFieldWidget = new TextFieldWidget(minecraft.textRenderer, 0,0, 100, 20, "");
            textFieldWidget.setMaxLength(1000);
            textFieldWidget.setText(scriptBinding.getScriptContent());
            textFieldWidget.method_1883(0);
            textFieldWidget.setChangedListener((text) -> {
                scriptBinding.setScriptContent(text);
            });

            buttonWidget = new ButtonWidget(0, 0, 100, 20, I18n.translate("scriptit:bindings.remove"), (button) -> {
                Resolver.getInstance().resolve(ScriptBindings.class).remove(scriptBinding);
                parent.removeEntry(scriptBinding);
            });
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(keyBindingButtonWidget, textFieldWidget, buttonWidget);
        }

        @Override
        public void render(int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float delta) {
            keyBindingButtonWidget.x = x + 5;
            keyBindingButtonWidget.y = y + 2;

            textFieldWidget.x = x + 85;
            textFieldWidget.y = y + 2;
            textFieldWidget.setWidth(width - 195);

            buttonWidget.x = x + width - 105;
            buttonWidget.y = y + 2;

            for (Element element : children()) {
                if (element instanceof Drawable) {
                    ((Drawable) element).render(mouseX, mouseY, delta);
                }
            }
        }
    }
}
