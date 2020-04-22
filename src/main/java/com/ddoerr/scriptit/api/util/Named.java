package com.ddoerr.scriptit.api.util;

import java.util.Map;

public class Named<T> {
    private String name;
    private T value;

    public static <T> Named<T> of(String name, T value) {
        return new Named<>(name, value);
    }

    public static <T> Named<T> of(Map.Entry<String, T> entry) {
        return new Named<>(entry.getKey(), entry.getValue());
    }

    public Named(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
