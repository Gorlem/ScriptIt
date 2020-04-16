package com.ddoerr.scriptit.libraries.settings;

import com.google.common.base.Joiner;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringSetting extends AbstractSetting<String> implements ToggleableSetting {
    private List<String> whitelist = new ArrayList<>();
    private Joiner joiner = Joiner.on(", ");

    public StringSetting(String name, Supplier<String> getter, Consumer<String> setter) {
        super(name, getter, setter);
    }

    public StringSetting(String name, Supplier<String> getter, Consumer<String> setter, List<String> whitelist) {
        super(name, getter, setter);
        this.whitelist = whitelist;
    }

    @Override
    public void set(Object value) {
        if (!whitelist.isEmpty() && !whitelist.contains(value)) {
            return;
        }
        setter.accept((String)value);
    }

    @Override
    public String getPossibleValues() {
        return joiner.join(whitelist);
    }

    @Override
    public Type getType() {
        return String.class;
    }

    @Override
    public Object toggle() {
        if (whitelist.isEmpty()) {
            return null;
        }

        int index = whitelist.indexOf(getter.get());

        if (index == -1) {
            return null;
        }

        String next = whitelist.get((index + 1) % whitelist.size());
        setter.accept(next);
        return next;
    }
}
