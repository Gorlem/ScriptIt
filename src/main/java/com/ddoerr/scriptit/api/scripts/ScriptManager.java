package com.ddoerr.scriptit.api.scripts;

import com.ddoerr.scriptit.scripts.ScriptContainer;

import java.util.List;

public interface ScriptManager {
    void add(ScriptContainer scriptContainer);

    List<ScriptContainer> getAll();

    void remove(ScriptContainer scriptContainer);
}
