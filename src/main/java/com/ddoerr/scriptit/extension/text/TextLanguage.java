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
        String result = substitutor.replace(script.getScriptSource().getContent());
        return CompletableFuture.completedFuture(new TextContainedValue(result));
    }

    @Override
    public String lookup(String key) {
        String[] parts = key.split("\\.");

        Model model = libraries.get(parts[0]);
        if (model == null) {
            return null;
        }

        for (int i = 1; i < parts.length; i++) {
            if (model.hasGetter(parts[i])) {
                Object object = model.runGetter(parts[i], factory);
                if (object instanceof Model) {
                    model = (Model)object;
                } else if (object == null) {
                    return null;
                } else {
                    return object.toString();
                }
            } else {
                return null;
            }
        }

        return null;
    }
}
