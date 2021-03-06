package com.ddoerr.scriptit.api.scripts;

import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ScriptManager {
    CompletableFuture<ContainedValue> runScript(Script script);
    void runScriptContainer(ScriptContainer scriptContainer, TriggerMessage triggerMessage);
    List<RunningScript> getRunningScripts();
    int stopScripts(String name);
    int stopAllScripts();
}
