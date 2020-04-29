package com.ddoerr.scriptit.api.scripts;

public class StringScriptSource implements ScriptSource {
    String content;

    public StringScriptSource(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
