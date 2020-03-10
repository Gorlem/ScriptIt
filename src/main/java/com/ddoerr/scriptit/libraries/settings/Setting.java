package com.ddoerr.scriptit.libraries.settings;

public interface Setting {
    String getName();
    Object get();
    void set(Object object);
    String getPossibleValues();
}
