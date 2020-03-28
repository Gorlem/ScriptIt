package com.ddoerr.scriptit.api.dependencies;

import com.ddoerr.scriptit.api.exceptions.DependencyException;

import java.util.List;

public interface Resolver {
    static Resolver getInstance() {
        return ResolverImpl.getInstance();
    }

    <T> void add(T dependency);

    <T> T resolve(Class<T> dependencyClass) throws DependencyException;
    <T> List<T> resolveAll(Class<T> dependencyClass);

    <T> T create(Class<T> type) throws DependencyException;
}
