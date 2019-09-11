package com.ddoerr.scriptit.dependencies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResolverImpl implements Resolver {
    List dependencies = new ArrayList();

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
        for (Object dependency : dependencies) {
            if (dependencyClass.isInstance(dependency)) {
                return (T)dependency;
            }
        }

        return null;
    }

    @Override
    public <T> Collection<T> resolveAll(Class<T> dependencyClass) {
        List<T> list = new ArrayList<>();

        for (Object dependency : dependencies) {
            if (dependencyClass.isInstance(dependency)) {
                list.add((T)dependency);
            }
        }

        return list;
    }
}
