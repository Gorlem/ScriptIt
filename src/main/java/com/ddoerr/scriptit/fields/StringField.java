package com.ddoerr.scriptit.fields;

import spinnery.widget.WTextField;

public class StringField extends AbstractTextField<String> {
    @Override
    public void deserialize(String value) {
        this.value = value;
    }

    @Override
    public String serialize() {
        return value;
    }

    @Override
    protected String getTemporaryValue(WTextField widget) {
        return widget.getText();
    }
}
