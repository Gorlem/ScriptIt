package com.ddoerr.scriptit.loader.container;

import com.ddoerr.scriptit.api.libraries.FunctionExecutor;
import com.ddoerr.scriptit.api.libraries.VariableUpdater;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;

import java.util.*;

public class NamespaceRegistryImpl implements NamespaceRegistry {
    private String name;

    private Map<String, FunctionExecutor> functions = new HashMap<>();
    private Map<String, VariableUpdater> variables = new HashMap<>();
    private List<NamespaceRegistry> namespaces = new ArrayList<>();

    public NamespaceRegistryImpl(String name) {
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
        NamespaceRegistryImpl namespace = new NamespaceRegistryImpl(name);
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
