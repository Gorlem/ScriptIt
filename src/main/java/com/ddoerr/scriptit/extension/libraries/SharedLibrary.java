package com.ddoerr.scriptit.extension.libraries;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;

import java.util.HashMap;
import java.util.Map;

public class SharedLibrary extends AnnotationBasedModel {
    private Map<String, ContainedValue> map = new HashMap<>();

    @Callable
    public void set(String key, ContainedValue value) {
        map.put(key, value);
    }

    @Callable
    public ContainedValue get(String key) {
        return map.get(key);
    }
}
