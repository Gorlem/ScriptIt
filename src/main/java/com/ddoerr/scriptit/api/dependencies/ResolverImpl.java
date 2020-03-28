package com.ddoerr.scriptit.api.dependencies;

import com.ddoerr.scriptit.api.exceptions.ConversionException;
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

    public ResolverImpl() {
        dependencies.add(this);
    }

    @Override
    public <T> void add(T dependency) {
        dependencies.add(dependency);
    }

    @Override
    public <T> T resolve(Class<T> dependencyClass) throws DependencyException {
        return dependencies.stream()
                .filter(dependencyClass::isInstance)
                .map(dependencyClass::cast).findFirst()
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
    public <T> T create(Class<T> type) throws DependencyException {
        Constructor<?>[] constructors = type.getConstructors();

        Arrays.sort(constructors, Comparator.comparingInt(Constructor::getParameterCount));

        Constructor<?> choosenConstructor = null;
        List<Object> resolvedParameters = new ArrayList<>();

        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameters = constructor.getParameterTypes();
            resolvedParameters.clear();
            for (Class<?> parameter : parameters) {
                try {
                    resolvedParameters.add(resolve(parameter));
                } catch (Exception e) {
                    break;
                }
            }

            if (resolvedParameters.size() == parameters.length) {
                choosenConstructor = constructor;
                break;
            }
        }

        if (choosenConstructor == null) {
            throw new DependencyException("Can't find resolvable constructor for " + type.getCanonicalName());
        }


        try {
            return (T) choosenConstructor.newInstance(resolvedParameters.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        throw new DependencyException("Could not create constructor for " + type.getCanonicalName());
    }
}
