package com.ddoerr.scriptit.api.languages;

import com.ddoerr.scriptit.api.libraries.Library;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptThread;

import java.util.Collection;

public interface Language {
    Collection<String> getExtensions();
    void loadLibrary(Library library);

    ContainedValue runScriptInstantly(Script script);
    ScriptThread runScriptThreaded(Script script);
}
