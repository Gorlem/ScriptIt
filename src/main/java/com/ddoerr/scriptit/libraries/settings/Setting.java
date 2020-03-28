package com.ddoerr.scriptit.libraries.settings;

import java.lang.reflect.Type;

public interface Setting {
    String getName();
    Object get();
    void set(Object object);
    String getPossibleValues();
    Type getType();
}
