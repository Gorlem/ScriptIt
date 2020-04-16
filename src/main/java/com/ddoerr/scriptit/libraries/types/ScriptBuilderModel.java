package com.ddoerr.scriptit.libraries.types;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.api.scripts.LifeCycle;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;

public class ScriptBuilderModel extends AnnotationBasedModel {
    private ScriptBuilder scriptBuilder = new ScriptBuilder();

    public ScriptBuilderModel() {
        scriptBuilder.lifeCycle(LifeCycle.Threaded);
    }

    @Callable
    public ScriptBuilderModel language(String language) {
        scriptBuilder.language(language);
        return this;
    }

    @Callable
    public ScriptBuilderModel script(String content) {
        scriptBuilder.fromString(content);
        return this;
    }

    @Callable
    public ScriptBuilderModel file(String path) {
        scriptBuilder.fromFile(path);
        return this;
    }

    @Callable
    public ScriptBuilderModel instant() {
        scriptBuilder.lifeCycle(LifeCycle.Instant);
        return this;
    }

    @Callable
    public String run() {
        return scriptBuilder.run();
    }
}
