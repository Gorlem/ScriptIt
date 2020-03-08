package com.ddoerr.scriptit.util.settings;

public interface Setting {
    String getName();
    Object get();
    void set(Object object);
    String getPossibleValues();
}
