package ml.gorlem.scriptit.api.scripts;

import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;

import java.util.Collection;

public interface Script {
    default boolean hasFileSource() {
        return getFileSource() != null;
    }
    default boolean hasStringSource() {
        return getStringSource() != null;
    }

    String getFileSource();
    String getStringSource();

    Collection<NamespaceRegistry> getAdditionalRegistries();

    Object runInstantly();
    ScriptThread runThreaded();

    String getName();
}
