package com.ddoerr.scriptit.extension.text;

import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.libraries.Model;
import com.ddoerr.scriptit.api.scripts.Script;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TextLanguage implements Language, StringLookup {
    public static final String SEPARATOR_REGEX = "\\.";

    private Map<String, Model> libraries = new HashMap<>();
    private TextContainedResultFactory factory = new TextContainedResultFactory();
    private StringSubstitutor substitutor = new StringSubstitutor(this, "{{", "}}", '{');

    @Override
    public Collection<String> getExtensions() {
        return Collections.singletonList(".txt");
    }

    @Override
    public void loadLibrary(String name, Model model) {
        libraries.put(name, model);
    }

    @Override
    public CompletableFuture<ContainedValue> runScript(Script script) {
        for (Map.Entry<String, Model> entry : script.getLibraries().entrySet()) {
            libraries.put(entry.getKey(), entry.getValue());
        }

        String result = substitutor.replace(script.getScriptSource().getContent());

        for (Map.Entry<String, Model> entry : script.getLibraries().entrySet()) {
            libraries.put(entry.getKey(), null);
        }

        return CompletableFuture.completedFuture(new TextContainedValue(result));
    }

    @Override
    public String lookup(String key) {
        String[] parts = key.split(SEPARATOR_REGEX);

        Model model = libraries.get(parts[0]);
        if (model == null) {
            return null;
        }

        for (int i = 1; i < parts.length; i++) {
            Object object = null;

            try {
                if (model.hasGetter(parts[i])) {
                    object = model.runGetter(parts[i], factory);
                } else if (model.hasFunction(parts[i])) {
                    object = model.runFunction(parts[i], new ContainedValue[0], factory);
                }
            } catch (Exception ignored) { }

            if (object == null) {
                return null;
            } else if (object instanceof Model) {
                model = (Model) object;
            } else {
                return object.toString();
            }
        }

        return null;
    }
}
