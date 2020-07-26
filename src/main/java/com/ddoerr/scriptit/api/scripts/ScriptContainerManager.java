package com.ddoerr.scriptit.api.scripts;

import java.util.List;

public interface ScriptContainerManager {
    void add(ScriptContainer scriptContainer);
    List<ScriptContainer> getAll();
    void remove(ScriptContainer scriptContainer);
}
