package com.ddoerr.scriptit.api.scripts;

import java.io.File;

public interface ScriptSource {
    static ScriptSource From(String content) {
        return new StringScriptSource(content);
    }

    static ScriptSource From(File file) {
        return new FileScriptSource(file);
    }

    String getContent();
}
