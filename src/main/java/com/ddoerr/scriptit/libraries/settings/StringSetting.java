package com.ddoerr.scriptit.libraries.settings;

import com.ddoerr.scriptit.api.util.ObjectConverter;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringSetting extends AbstractSetting<String> {
    private List<String> whitelist = new ArrayList<>();
    private Joiner joiner = Joiner.on(", ");

    public StringSetting(String name, Supplier<String> getter, Consumer<String> setter) {
        super(name, getter, setter);
    }

    public StringSetting(String name, Supplier<String> getter, Consumer<String> setter, List<String> whitelist) {
        super(name, getter, setter);
        this.whitelist = whitelist;
    }

    public void setWhitelist(List<String> whitelist) {
        this.whitelist = whitelist;
    }

    @Override
    public void set(Object object) {
        String value = ObjectConverter.toString(object);
        if (!whitelist.isEmpty() && !whitelist.contains(value)) {
            return;
        }

        setter.accept(value);
    }

    @Override
    public String getPossibleValues() {
        return joiner.join(whitelist);
    }
}
