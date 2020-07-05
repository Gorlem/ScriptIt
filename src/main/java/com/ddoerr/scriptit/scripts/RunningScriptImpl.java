package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.scripts.RunningScript;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;

import java.util.concurrent.CompletableFuture;

public class RunningScriptImpl implements RunningScript {
    ScriptContainer scriptContainer;
    CompletableFuture<ContainedValue> future;

    public RunningScriptImpl(ScriptContainer scriptContainer, CompletableFuture<ContainedValue> future) {
        this.scriptContainer = scriptContainer;
        this.future = future;
    }

    @Override
    public ScriptContainer getScriptContainer() {
        return scriptContainer;
    }

    @Override
    public CompletableFuture<ContainedValue> getFuture() {
        return future;
    }
}
