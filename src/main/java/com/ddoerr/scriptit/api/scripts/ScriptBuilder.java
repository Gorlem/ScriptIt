package com.ddoerr.scriptit.api.scripts;

import com.ddoerr.scriptit.api.dependencies.LanguageLoader;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.libraries.Library;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScriptBuilder implements Script {
    private Language language;
    private String path;
    private String content;
    private List<Library> libraries = new ArrayList<>();
    private String name;
    private LifeCycle lifeCycle;

    private LanguageLoader languageLoader;
    private ThreadLifetimeManager threadLifetimeManager;

    public ScriptBuilder() {
        Resolver resolver = Resolver.getInstance();
        try {
            languageLoader = resolver.resolve(LanguageLoader.class);
            threadLifetimeManager = resolver.resolve(ThreadLifetimeManager.class);

            language = languageLoader.findByName("lua");
            name = "main";
            lifeCycle = LifeCycle.Instant;
        } catch (DependencyException e) {
            e.printStackTrace();
        }
    }

    public ScriptBuilder language(String language) {
        this.language = languageLoader.findByName(language);
        return this;
    }

    public ScriptBuilder fromString(String content) {
        this.content = content;
        this.path = null;
        return this;
    }

    public ScriptBuilder fromFile(String path) {
        this.path = path;
        this.content = null;

        String extension = FilenameUtils.getExtension(path);
        this.language = languageLoader.getLanguages().stream()
                .filter(l -> l.getExtensions().contains(extension))
                .findFirst()
                .orElse(null);

        return this;
    }

    public ScriptBuilder withLibrary(Library library) {
        if (library != null) {
            libraries.add(library);
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
    public String getFileSource() {
        return path;
    }

    @Override
    public String getStringSource() {
        return content;
    }

    @Override
    public Collection<Library> getLibraries() {
        return libraries;
    }

    @Override
    public String getName() {
        return name;
    }
}
