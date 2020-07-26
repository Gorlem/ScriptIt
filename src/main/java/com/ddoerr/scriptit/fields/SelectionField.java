package com.ddoerr.scriptit.fields;

import com.ddoerr.scriptit.screens.widgets.PanelWidget;
import com.ddoerr.scriptit.screens.widgets.ValuesDropdownWidget;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.ArrayList;
import java.util.List;

public class SelectionField extends AbstractField<String> {
    private List<String> values = new ArrayList<>();
    private String prefix;

    @Override
    public void deserialize(String value) {
        this.value = value;
    }

    @Override
    public String serialize() {
        return value;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void setTranslationPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void createWidget(PanelWidget panel) {
        super.createWidget(panel);

        ValuesDropdownWidget<String> dropdown = panel.createChild(ValuesDropdownWidget.class, Position.of(panel, 0, 10), Size.of(panel.getWidth(), 20));

        if (prefix != null) {
            dropdown.setTranslationPrefix(prefix);
        }

        if (value == null) {
            dropdown.setLabel("Please select a value");
        } else {
            dropdown.selectValue(value);
        }

        dropdown.addValues(values);
        dropdown.setOnChange(data -> temporaryValue = data);

        panel.setHeight(30);
    }
}
