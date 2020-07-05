package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.api.scripts.ScriptManager;

import java.util.List;
import java.util.stream.Collectors;

public class ScriptsLibrary extends AnnotationBasedModel {
    private ScriptManager scriptManager;

    public ScriptsLibrary(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Callable
    public int stop() {
        return scriptManager.stopAllScripts();
    }

    @Callable
    public int stop(String name) {
        return scriptManager.stopScripts(name);
    }

    @Getter
    public List<String> getRunningScripts() {
        return scriptManager.getRunningScripts().stream()
                .map(runningScript -> runningScript.getScriptContainer().getScript().getName())
                .collect(Collectors.toList());
    }
}
