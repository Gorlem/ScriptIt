package com.ddoerr.scriptit.api.libraries;

import java.util.List;

public interface NamespaceRegistry {
    Function registerFunction(String name, FunctionExecutor executor);
    Variable registerVariable(String name, VariableUpdater updater);
    NamespaceRegistry registerNamespace(String name);

    List<Function> getFunctions();
    List<Variable> getVariables();
    List<NamespaceRegistry> getNamespaces();

    String getName();
}
