package com.ddoerr.scriptit.fields;

import com.ddoerr.scriptit.screens.widgets.PanelWidget;
import spinnery.widget.WTextField;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public abstract class AbstractTextField<T> extends AbstractField<T> {
    @Override
    public void createWidget(PanelWidget panel) {
        super.createWidget(panel);

        panel.createChild(WTextField.class, Position.of(panel, 0, 10), Size.of(panel.getWidth(), 16))
                .setText(serialize())
                .setOnKeyPressed((widget, keyCode, character, keyModifier) -> {
                    temporaryValue = getTemporaryValue((WTextField)widget);
                })
                .setOnCharTyped((widget, character, keyCode) -> {
                    temporaryValue = getTemporaryValue((WTextField)widget);
                });

        panel.setHeight(26);
    }

    protected abstract T getTemporaryValue(WTextField widget);
}
