package com.ddoerr.scriptit.extension.text;

import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.libraries.Model;
import com.ddoerr.scriptit.api.scripts.Script;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextLanguage implements Language, StringLookup {
    public static final Pattern SEPARATOR_REGEX = Pattern.compile("\\.");
    public static final Pattern FUNCTION_REGEX = Pattern.compile("^(\\w+)\\(([\\w,]+)\\)$");
    public static final Pattern FUNCTION_ARGUMENTS_REGEX = Pattern.compile(",");

    private Map<String, Model> libraries = new HashMap<>();
    private TextContainedResultFactory factory = new TextContainedResultFactory();
    private StringSubstitutor substitutor;

    public TextLanguage() {
        substitutor = new StringSubstitutor(this, "{{", "}}", '{', "|");
        substitutor.setEnableSubstitutionInVariables(true);
    }

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
        String[] parts = SEPARATOR_REGEX.split(key);

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
                } else {
                    Matcher matcher = FUNCTION_REGEX.matcher(parts[i]);
                    if (matcher.matches() && model.hasFunction(matcher.group(1))) {
                        String functionName = matcher.group(1);
                        String argument = matcher.group(2);

                        TextContainedValue[] arguments = Stream.of(FUNCTION_ARGUMENTS_REGEX.split(argument))
                                .map(TextContainedValue::new)
                                .toArray(TextContainedValue[]::new);

                        object = model.runFunction(functionName, arguments, factory);

                        if (object == null) {
                            object = StringUtils.EMPTY;
                        }
                    }
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
