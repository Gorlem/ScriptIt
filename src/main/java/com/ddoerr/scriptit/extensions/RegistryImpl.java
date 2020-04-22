package com.ddoerr.scriptit.extensions;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.registry.Registry;
import com.ddoerr.scriptit.api.util.Named;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<Named<T>> getAll() {
        return registered.entrySet()
                .stream()
                .map(e -> Named.of(e))
                .collect(Collectors.toList());
    }
}
