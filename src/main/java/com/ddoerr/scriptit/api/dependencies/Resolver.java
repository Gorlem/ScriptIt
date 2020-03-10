package com.ddoerr.scriptit.api.dependencies;

import java.util.List;

public interface Resolver {
    static Resolver getInstance() {
        return ResolverImpl.getInstance();
    }

    <T> void add(T dependency);

    <T> T resolve(Class<T> dependencyClass);
    <T> List<T> resolveAll(Class<T> dependencyClass);
}
