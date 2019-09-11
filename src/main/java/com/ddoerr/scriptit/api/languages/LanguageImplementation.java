package com.ddoerr.scriptit.api.languages;

import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptThread;
import net.minecraft.util.Tickable;

import java.util.Collection;

public interface LanguageImplementation extends Tickable {
    Collection<String> getExtensions();
    void loadRegistry(NamespaceRegistry registry);

    Object runScriptInstantly(Script script);
    ScriptThread runScriptThreaded(Script script);
}
