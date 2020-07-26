package com.ddoerr.scriptit.fields;

import spinnery.widget.WTextField;

public class IntegerField extends AbstractTextField<Integer> {
    @Override
    public void deserialize(String value) {
        this.value = Integer.parseInt(value);
    }

    @Override
    public String serialize() {
        return value.toString();
    }

    @Override
    protected Integer getTemporaryValue(WTextField widget) {
        String integerText = widget.getText().replaceAll("\\D+", "");
        // TODO: Currently breaks spinnery
        // widget.setText(integerText);

        if (integerText.length() == 0) {
            return 0;
        }

        return Integer.parseInt(integerText);
    }
}
