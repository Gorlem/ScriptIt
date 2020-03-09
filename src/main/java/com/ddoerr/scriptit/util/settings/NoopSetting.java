package com.ddoerr.scriptit.util.settings;

public class NoopSetting implements Setting {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object get() {
        return null;
    }

    @Override
    public void set(Object object) {

    }

    @Override
    public String getPossibleValues() {
        return null;
    }
}
