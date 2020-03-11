package com.ddoerr.scriptit.api.libraries;

public interface Variable extends VariableUpdater {
    String getName();
    boolean isDisabled();
    void disable();
}
