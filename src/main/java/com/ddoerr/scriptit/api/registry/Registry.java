package com.ddoerr.scriptit.api.registry;

import com.ddoerr.scriptit.api.util.Named;

import java.util.List;

public interface Registry<T> {
    void register(String name, Class<? extends T> object);
    T findByName(String name);
    List<Named<T>> getAll();
}
