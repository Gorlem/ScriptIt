package com.ddoerr.scriptit.fields;

import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.screens.widgets.PanelWidget;
import spinnery.widget.WTextField;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class ColorField extends AbstractField<String> {
    @Override
    public void deserialize(String value) {
        this.value = value;
    }

    @Override
    public String serialize() {
        return value;
    }

    @Override
    public void createWidget(PanelWidget panel) {
        super.createWidget(panel);

        PanelWidget colorPanel = panel.createChild(PanelWidget.class, Position.of(panel, 0, 10), Size.of(16, 16));

        Color color = Color.runAndParse(value);
        colorPanel.setColor(spinnery.widget.api.Color.of(color.getValue()));


        panel.createChild(WTextField.class, Position.of(panel, 21, 10), Size.of(panel.getWidth() - 21, 16))
                .setText(serialize())
                .setOnKeyPressed((widget, keyCode, character, keyModifier) -> {
                    String text = ((WTextField) widget).getText();
                    Color color1 = Color.runAndParse(text);
                    colorPanel.setColor(spinnery.widget.api.Color.of(color1.getValue()));
                    temporaryValue = text;
                })
                .setOnCharTyped((widget, character, keyCode) -> {
                    String text = ((WTextField) widget).getText();
                    Color color1 = Color.runAndParse(text);
                    colorPanel.setColor(spinnery.widget.api.Color.of(color1.getValue()));
                    temporaryValue = text;
                });

        panel.setHeight(26);
    }
}
