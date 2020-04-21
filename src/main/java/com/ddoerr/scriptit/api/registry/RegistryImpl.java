package com.ddoerr.scriptit.api.registry;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistryImpl<T> implements Registry<T> {
    private Map<String, T> registered = new HashMap<>();

    private Resolver resolver;

    public RegistryImpl(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void register(String name, Class<? extends T> object) {
        try {
            registered.put(name, resolver.create(object));
        } catch (DependencyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T findByName(String name) {
        return registered.get(name);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(registered.values());
    }
}
