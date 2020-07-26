package com.ddoerr.scriptit.api.dependencies;

import com.ddoerr.scriptit.api.exceptions.DependencyException;

import java.util.List;
import java.util.function.Supplier;

public interface Resolver {
    static Resolver getInstance() {
        return ResolverImpl.getInstance();
    }

    <T> void add(T dependency);
    <T> void add(Class<T> dependencyClass) throws DependencyException;

    <T> T resolve(Class<T> dependencyClass) throws DependencyException;
    <T> List<T> resolveAll(Class<T> dependencyClass);

    <T> T create(Class<T> type, Object... parameters) throws DependencyException;

    <T> Supplier<T> supplier(Class<? extends T> derivedType, Object... parameters);
}
