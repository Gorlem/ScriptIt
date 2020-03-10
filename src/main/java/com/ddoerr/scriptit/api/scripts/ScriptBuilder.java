package com.ddoerr.scriptit.api.scripts;

import com.ddoerr.scriptit.api.dependencies.LanguageLoader;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.languages.LanguageImplementation;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScriptBuilder {
    private LanguageImplementation language;
    private String path;
    private String content;
    private List<NamespaceRegistry> registries = new ArrayList<>();
    private String name;
    private LifeCycle lifeCycle;

    private LanguageLoader languageLoader;

    public ScriptBuilder() {
        languageLoader = Resolver.getInstance().resolve(LanguageLoader.class);
        language = languageLoader.findByName("lua");
        name = "main";
        lifeCycle = LifeCycle.Instant;
    }

    public ScriptBuilder setLanguage(String language) {
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
        return this;
    }

    public ScriptBuilder withRegistry(NamespaceRegistry registry) {
        if (registry != null) {
            registries.add(registry);
        }
        return this;
    }

    public ScriptBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ScriptBuilder setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
        return this;
    }

    public Script build() {
        if (path == null && content == null) {
            return null;
        }

        return new ScriptImplementation(language, path, content, registries, name, lifeCycle);
    }

    static class ScriptImplementation implements Script {
        LanguageImplementation language;
        String path;
        String content;
        Collection<NamespaceRegistry> namespaceRegistries;
        private String name;
        LifeCycle lifeCycle;

        public ScriptImplementation(LanguageImplementation language, String path, String content, Collection<NamespaceRegistry> namespaceRegistries, String name, LifeCycle lifeCycle) {
            this.language = language;
            this.path = path;
            this.content = content;
            this.namespaceRegistries = namespaceRegistries;
            this.name = name;
            this.lifeCycle = lifeCycle;
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
        public Collection<NamespaceRegistry> getAdditionalRegistries() {
            return namespaceRegistries;
        }

        @Override
        public Object runInstantly() {
            if (language == null)
                return null;

            return language.runScriptInstantly(this);
        }

        @Override
        public ScriptThread runThreaded() {
            if (language == null)
                return null;

            return language.runScriptThreaded(this);
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
