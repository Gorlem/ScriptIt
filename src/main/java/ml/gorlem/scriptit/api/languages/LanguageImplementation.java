package ml.gorlem.scriptit.api.languages;

import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import ml.gorlem.scriptit.api.scripts.Script;
import ml.gorlem.scriptit.api.scripts.ScriptThread;
import net.minecraft.util.Tickable;

import java.util.Collection;

public interface LanguageImplementation extends Tickable {
    Collection<String> getExtensions();
    void loadRegistry(NamespaceRegistry registry);

    Object runScriptInstantly(Script script);
    ScriptThread runScriptThreaded(Script script);
}
