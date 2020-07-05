package com.ddoerr.scriptit.api.scripts;

import com.ddoerr.scriptit.api.libraries.Model;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ScriptBuilder implements Script {
    private Identifier language;
    private ScriptSource scriptSource;
    private Map<String, Model> libraries = new HashMap<>();
    private String name = "main";

    public ScriptBuilder() {

    }

    public ScriptBuilder(Script script) {
        language = script.getLanguage();
        scriptSource = script.getScriptSource();
        libraries = new HashMap<>(script.getLibraries());
        name = script.getName();
    }

    public ScriptBuilder language(String language) {
        this.language = new Identifier(language);
        return this;
    }

    public ScriptBuilder fromString(String content) {
        this.scriptSource = ScriptSource.From(content);
        return this;
    }

    public ScriptBuilder fromFile(String path) {
        this.scriptSource = ScriptSource.From(new File(path));
        return this;
    }

    public ScriptBuilder withLibrary(String name, Model model) {
        if (model != null) {
            libraries.put(name, model);
        }
        return this;
    }

    public ScriptBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ScriptSource getScriptSource() {
        return scriptSource;
    }

    @Override
    public Map<String, Model> getLibraries() {
        return libraries;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Identifier getLanguage() {
        return language;
    }
}
