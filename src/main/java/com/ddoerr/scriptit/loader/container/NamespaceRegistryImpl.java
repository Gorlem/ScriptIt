package com.ddoerr.scriptit.loader.container;

import com.ddoerr.scriptit.api.libraries.*;

import java.util.*;

public class NamespaceRegistryImpl implements NamespaceRegistry {
    private String name;

    private List<Function> functions = new ArrayList<>();
    private List<Variable> variables = new ArrayList<>();
    private List<NamespaceRegistry> namespaces = new ArrayList<>();

    public NamespaceRegistryImpl(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Function registerFunction(String name, FunctionExecutor executor) {
        Function function = new FunctionImpl(name, executor);
        functions.add(function);
        return function;
    }

    @Override
    public Variable registerVariable(String name, VariableUpdater updater) {
        Variable variable = new VariableImpl(name, updater);
        variables.add(variable);
        return variable;
    }

    @Override
    public NamespaceRegistry registerNamespace(String name) {
        NamespaceRegistryImpl namespace = new NamespaceRegistryImpl(name);
        namespaces.add(namespace);
        return namespace;
    }

    @Override
    public List<Function> getFunctions() {
        return functions;
    }

    @Override
    public List<Variable> getVariables() {
        return variables;
    }

    @Override
    public List<NamespaceRegistry> getNamespaces() {
        return namespaces;
    }
}
