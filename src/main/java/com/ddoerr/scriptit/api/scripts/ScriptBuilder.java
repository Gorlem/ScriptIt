package com.ddoerr.scriptit.api.scripts;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.libraries.Model;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.*;

public class ScriptBuilder implements Script {
    private Language language;
    private ScriptSource scriptSource;
    private Map<String, Model> libraries = new HashMap<>();
    private String name;
    private LifeCycle lifeCycle;

    private ScriptItRegistry registry;
    private ThreadLifetimeManager threadLifetimeManager;

    public ScriptBuilder() {
        Resolver resolver = Resolver.getInstance();
        try {
            registry = resolver.resolve(ScriptItRegistry.class);
            threadLifetimeManager = resolver.resolve(ThreadLifetimeManager.class);

            language = registry.languages.get(registry.languages.getDefaultId());
            name = "main";
            lifeCycle = LifeCycle.Instant;
        } catch (DependencyException e) {
            e.printStackTrace();
        }
    }

    public ScriptBuilder language(String language) {
        this.language = registry.languages.get(new Identifier(language));
        return this;
    }

    public ScriptBuilder fromString(String content) {
        this.scriptSource = ScriptSource.From(content);
        return this;
    }

    public ScriptBuilder fromFile(String path) {
        this.scriptSource = ScriptSource.From(new File(path));

        String extension = FilenameUtils.getExtension(path);
        this.language = registry.languages
                .stream()
                .filter(language -> language.getExtensions().contains(extension))
                .findFirst()
                .orElse(null);

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

    public ScriptBuilder lifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
        return this;
    }

    public String run() {
        switch (lifeCycle) {
            case Instant:
                return language.runScriptInstantly(this).format();
            case Threaded:
                ScriptThread scriptThread = language.runScriptThreaded(this);
                threadLifetimeManager.watch(scriptThread);
                return null;
        }
        return null;
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
}
