package com.ddoerr.scriptit.api.registry;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;

import java.util.HashMap;
import java.util.Map;

public class ExtensionImpl implements Extension {
    private Map<Class<?>, Registry<?>> registries = new HashMap<>();

    private Resolver resolver;

    public ExtensionImpl(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public <T> Extension register(Class<T> registryType, String name, Class<? extends T> model) {
        if (!registries.containsKey(registryType)) {
            try {
                registries.put(registryType, resolver.create(RegistryImpl.class));
            } catch (DependencyException e) {
                e.printStackTrace();
            }
        }

        Registry<T> registry = (Registry<T>)registries.get(registryType);
        registry.register(name, model);
        return this;
    }
}
