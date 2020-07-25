package com.ddoerr.scriptit.fields;

import com.ddoerr.scriptit.screens.widgets.KeyBindingButtonWidget;
import com.ddoerr.scriptit.screens.widgets.PanelWidget;
import net.minecraft.client.util.InputUtil;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class KeyBindingField extends AbstractField<InputUtil.KeyCode> {
    @Override
    public void deserialize(String value) {
        this.value = InputUtil.fromName(value);
    }

    @Override
    public String serialize() {
        return value.getName();
    }

    @Override
    public void createWidget(PanelWidget panel) {
        super.createWidget(panel);

        KeyBindingButtonWidget keyBindingButtonWidget = panel.createChild(KeyBindingButtonWidget.class, Position.of(panel, 0, 10), Size.of(panel.getWidth(), 20));
        keyBindingButtonWidget.setOnChange(keyCode -> temporaryValue = keyCode);

        if (value != null) {
            keyBindingButtonWidget.setKeyCode(value);
        }
    }
}
