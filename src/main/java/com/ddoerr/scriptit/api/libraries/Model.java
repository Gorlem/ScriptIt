package com.ddoerr.scriptit.api.libraries;

import com.ddoerr.scriptit.api.languages.ContainedResultFactory;
import com.ddoerr.scriptit.api.languages.ContainedValue;

public interface Model {
    boolean hasGetter(String key);
    boolean hasSetter(String key);
    boolean hasFunction(String key);

    <T> T runGetter(String key, ContainedResultFactory<T> factory);
    void runSetter(String key, ContainedValue value);
    <T> T runFunction(String key, ContainedValue[] values, ContainedResultFactory<T> factory);
}
