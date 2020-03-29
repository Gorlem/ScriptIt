package com.ddoerr.scriptit.api.dependencies;

import com.ddoerr.scriptit.api.exceptions.DependencyException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ResolverImpl implements Resolver {
    List<Object> dependencies = new ArrayList<>();

    private static Resolver instance;
    public static Resolver getInstance() {
        if (instance == null)
            instance = new ResolverImpl();

        return instance;
    }

    private void add(ResolverImpl resolver) {
        for (Object dependency : resolver.dependencies) {
            add(dependency);
        }
    }

    @Override
    public <T> void add(T dependency) {
        dependencies.add(dependency);
    }

    @Override
    public <T> void add(Class<T> dependencyClass) throws DependencyException {
        add(create(dependencyClass));
    }

    @Override
    public <T> T resolve(Class<T> dependencyClass) throws DependencyException {
        return dependencies.stream()
                .filter(dependencyClass::isInstance)
                .map(dependencyClass::cast)
                .findFirst()
                .orElseThrow(() -> new DependencyException("Can't find implementation for " + dependencyClass.getCanonicalName()));
    }

    @Override
    public <T> List<T> resolveAll(Class<T> dependencyClass) {
        return dependencies.stream()
                .filter(dependencyClass::isInstance)
                .map(dependencyClass::cast)
                .collect(Collectors.toList());
    }

    @Override
    public <T> T create(Class<T> type, Object... parameters) throws DependencyException {
        ResolverImpl creationResolver = new ResolverImpl();
        creationResolver.add(this);
        for (Object parameter : parameters) {
            creationResolver.add(parameter);
        }

        Constructor<?>[] constructors = type.getConstructors();

        Arrays.sort(constructors, Comparator.<Constructor<?>>comparingInt(Constructor::getParameterCount).reversed());

        Constructor<?> chosenConstructor = null;
        List<Object> resolvedParameters = new ArrayList<>();

        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            resolvedParameters.clear();
            for (Class<?> parameterType : parameterTypes) {
                try {
                    resolvedParameters.add(creationResolver.resolve(parameterType));
                } catch (Exception e) {
                    break;
                }
            }

            if (resolvedParameters.size() == parameterTypes.length) {
                chosenConstructor = constructor;
                break;
            }
        }

        if (chosenConstructor == null) {
            throw new DependencyException("Can't find resolvable constructor for " + type.getCanonicalName());
        }


        try {
            return (T) chosenConstructor.newInstance(resolvedParameters.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        throw new DependencyException("Could not create constructor for " + type.getCanonicalName());
    }
}
