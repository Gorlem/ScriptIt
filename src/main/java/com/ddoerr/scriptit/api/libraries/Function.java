package com.ddoerr.scriptit.api.libraries;

public interface Function extends FunctionExecutor {
    String getName();
    boolean isDisabled();
    void disable();
}
