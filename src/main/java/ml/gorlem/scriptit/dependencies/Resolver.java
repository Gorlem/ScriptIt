package ml.gorlem.scriptit.dependencies;

import java.util.Collection;

public interface Resolver {
    static Resolver getInstance() {
        return ResolverImpl.getInstance();
    }

    <T> void add(T dependency);

    <T> T resolve(Class<T> dependencyClass);
    <T> Collection<T> resolveAll(Class<T> dependencyClass);
}
