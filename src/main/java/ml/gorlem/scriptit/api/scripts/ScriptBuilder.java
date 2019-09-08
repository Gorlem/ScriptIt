package ml.gorlem.scriptit.api.scripts;

import ml.gorlem.scriptit.ScriptItMod;
import ml.gorlem.scriptit.api.languages.LanguageImplementation;
import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.loader.LanguageLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScriptBuilder {
    private LanguageImplementation language;
    private String path;
    private String content;
    private List<NamespaceRegistry> registries = new ArrayList<>();
    private String name;

    private LanguageLoader languageLoader;

    public ScriptBuilder() {
        languageLoader = Resolver.getInstance().resolve(LanguageLoader.class);
        language = languageLoader.findByName("lua");
        name = "main";
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

    public Script build() throws Exception {
        if (path == null && content == null) {
            throw new Exception();
        }

        return new ScriptImplementation(language, path, content, registries, name);
    }

    static class ScriptImplementation implements Script {
        LanguageImplementation language;
        String path;
        String content;
        Collection<NamespaceRegistry> namespaceRegistries;
        private String name;

        public ScriptImplementation(LanguageImplementation language, String path, String content, Collection<NamespaceRegistry> namespaceRegistries, String name) {
            this.language = language;
            this.path = path;
            this.content = content;
            this.namespaceRegistries = namespaceRegistries;
            this.name = name;
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
            return language.runScriptInstantly(this);
        }

        @Override
        public ScriptThread runThreaded() {
            return language.runScriptThreaded(this);
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
