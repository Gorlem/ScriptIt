package com.ddoerr.scriptit.api.registry;

import java.util.List;

public interface Registry<T> {
    void register(String name, Class<? extends T> object);
    T findByName(String name);
    List<T> getAll();
}
