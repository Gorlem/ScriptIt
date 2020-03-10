package com.ddoerr.scriptit.api.dependencies;

import java.util.ArrayList;
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

    @Override
    public <T> void add(T dependency) {
        dependencies.add(dependency);
    }

    @Override
    public <T> T resolve(Class<T> dependencyClass) {
        return dependencies.stream().filter(dependencyClass::isInstance).map(dependencyClass::cast).findFirst().orElse(null);
    }

    @Override
    public <T> List<T> resolveAll(Class<T> dependencyClass) {
        return dependencies.stream().filter(dependencyClass::isInstance).map(dependencyClass::cast).collect(Collectors.toList());
    }
}
