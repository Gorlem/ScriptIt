package com.ddoerr.scriptit.api.scripts;

import com.ddoerr.scriptit.api.languages.ContainedValue;

import java.util.concurrent.CompletableFuture;

public interface RunningScript {
    ScriptContainer getScriptContainer();
    CompletableFuture<ContainedValue> getFuture();
}
