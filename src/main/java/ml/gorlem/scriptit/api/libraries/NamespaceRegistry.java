package ml.gorlem.scriptit.api.libraries;

import java.util.Collection;
import java.util.Map;

public interface NamespaceRegistry {
    void registerFunction(String name, FunctionExecutor executor);
    void registerVariable(String name, VariableUpdater updater);
    NamespaceRegistry registerNamespace(String name);

    Map<String, FunctionExecutor> getFunctions();
    Map<String, VariableUpdater> getVariables();
    Collection<NamespaceRegistry> getNamespaces();

    String getName();
}
