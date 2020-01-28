package com.ddoerr.scriptit.screens;

import com.ddoerr.scriptit.scripts.LifeCycle;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.triggers.BusTrigger;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.LiteralText;
import org.apache.commons.lang3.StringUtils;

public class ScriptEditorScreen extends Screen {
    String eventBus = StringUtils.EMPTY;
    TextFieldWidget eventBusField;

    String lifeCycle = LifeCycle.Instant.toString();
    TextFieldWidget lifeCycleField;

    String script = StringUtils.EMPTY;
    TextFieldWidget scriptField;

    public ScriptEditorScreen() {
        super(new LiteralText("Script Editor"));
    }

    public ScriptEditorScreen(ScriptContainer scriptContainer) {
        super(new LiteralText("Script Editor"));

        eventBus = ((BusTrigger)scriptContainer.getTrigger()).getId();
        lifeCycle = scriptContainer.getLifeCycle().toString();
        script = scriptContainer.getContent();
    }

    @Override
    protected void init() {
        super.init();
        minecraft.keyboard.enableRepeatEvents(true);

        TextRenderer textRenderer = minecraft.textRenderer;

        eventBusField = new TextFieldWidget(textRenderer, 20, 20, 200, 20, "");
        eventBusField.setMaxLength(1000);
        eventBusField.setText(eventBus);
        eventBusField.setChangedListener(text -> eventBus = text);
        children.add(eventBusField);

        lifeCycleField = new TextFieldWidget(textRenderer, 20, 50, 200, 20, "");
        lifeCycleField.setMaxLength(1000);
        lifeCycleField.setText(lifeCycle);
        lifeCycleField.setChangedListener(text -> lifeCycle = text);
        children.add(lifeCycleField);

        scriptField = new TextFieldWidget(textRenderer, 20, 80, 200, 20, "");
        scriptField.setMaxLength(1000);
        scriptField.setText(script);
        scriptField.setChangedListener(text -> script = text);
        children.add(scriptField);

    }

    @Override
    public void removed() {
        super.removed();
        minecraft.keyboard.enableRepeatEvents(false);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        for (Element child : children) {
            if (child instanceof Drawable) {
                ((Drawable) child).render(mouseX, mouseY, delta);
            }
        }
    }
}
