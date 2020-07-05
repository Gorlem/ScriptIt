package com.ddoerr.scriptit.api.languages;

import com.ddoerr.scriptit.api.libraries.Model;
import com.ddoerr.scriptit.api.scripts.Script;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface Language {
    Collection<String> getExtensions();
    void loadLibrary(String name, Model model);
    CompletableFuture<ContainedValue> runScript(Script script);
}
