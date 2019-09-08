package ml.gorlem.scriptit.loader;

import ml.gorlem.scriptit.api.libraries.FunctionExecutor;
import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import ml.gorlem.scriptit.api.libraries.VariableUpdater;

import java.util.*;

public class NamespaceRegistryContainer implements NamespaceRegistry {
    private String name;

    private Map<String, FunctionExecutor> functions = new HashMap<>();
    private Map<String, VariableUpdater> variables = new HashMap<>();
    private List<NamespaceRegistry> namespaces = new ArrayList<>();

    public NamespaceRegistryContainer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void registerFunction(String name, FunctionExecutor executor) {
        functions.put(name, executor);
    }

    @Override
    public void registerVariable(String name, VariableUpdater updater) {
        variables.put(name, updater);
    }

    @Override
    public NamespaceRegistry registerNamespace(String name) {
        NamespaceRegistryContainer namespace = new NamespaceRegistryContainer(name);
        namespaces.add(namespace);
        return namespace;
    }

    public Map<String, FunctionExecutor> getFunctions() {
        return functions;
    }

    public Map<String, VariableUpdater> getVariables() {
        return variables;
    }

    public Collection<NamespaceRegistry> getNamespaces() {
        return namespaces;
    }
}
