package com.ddoerr.scriptit.api.scripts;

import com.ddoerr.scriptit.api.libraries.Model;
import net.minecraft.util.Identifier;

import java.util.Map;

public interface Script {
    ScriptSource getScriptSource();
    Map<String, Model> getLibraries();
    String getName();
    Identifier getLanguage();
}
