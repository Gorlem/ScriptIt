package com.ddoerr.scriptit.libraries.settings;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.annotations.Setter;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringSettingModel extends AbstractSettingModel<String> implements ToggleableSettingModel<String> {
    private List<String> whitelist = new ArrayList<>();
    private Joiner joiner = Joiner.on(", ");

    public StringSettingModel(String name, Supplier<String> getter, Consumer<String> setter) {
        super(name, getter, setter);
    }

    public StringSettingModel(String name, Supplier<String> getter, Consumer<String> setter, List<String> whitelist) {
        super(name, getter, setter);
        this.whitelist = whitelist;
    }

    @Setter
    @Override
    public void setValue(String value) {
        if (!whitelist.isEmpty() && !whitelist.contains(value)) {
            return;
        }
        super.setValue(value);
    }

    @Getter
    @Override
    public String getValue() {
        return super.getValue();
    }

    @Override
    public String getPossibleValues() {
        return joiner.join(whitelist);
    }

    @Callable
    @Override
    public String toggle() {
        if (whitelist.isEmpty()) {
            return null;
        }

        int index = whitelist.indexOf(getValue());

        if (index == -1) {
            return null;
        }

        String next = whitelist.get((index + 1) % whitelist.size());
        setValue(next);
        return next;
    }
}
