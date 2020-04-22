package com.ddoerr.scriptit.extensions;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.registry.Extension;
import com.ddoerr.scriptit.api.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class ExtensionImpl implements Extension {
    private Map<Class<?>, Registry<?>> registries = new HashMap<>();

    private Resolver resolver;

    public ExtensionImpl(Resolver resolver) {
        this.resolver = resolver;
    }

    private <T> Registry<T> getOrCreateRegistry(Class<T> registryType) {
        if (!registries.containsKey(registryType)) {
            try {
                Registry<T> registry = (Registry<T>)resolver.create(RegistryImpl.class);
                registries.put(registryType, registry);
                return registry;
            } catch (DependencyException e) {
                e.printStackTrace();
            }
        }

        return (Registry<T>)registries.get(registryType);
    }

    @Override
    public <T> Extension register(Class<T> registryType, String name, Class<? extends T> model) {
        Registry<T> registry = getOrCreateRegistry(registryType);
        registry.register(name, model);
        return this;
    }

    public <T> Registry<T> getRegistry(Class<T> registryType) {
        return getOrCreateRegistry(registryType);
    }
}
