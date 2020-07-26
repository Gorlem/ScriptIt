package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.api.scripts.ScriptContainerManager;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.api.scripts.ScriptManager;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class ScriptContainerManagerImpl implements Tickable, ScriptContainerManager {
    private List<ScriptContainer> scripts = new ArrayList<>();

    private ScriptManager scriptManager;

    public ScriptContainerManagerImpl(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public void add(ScriptContainer scriptContainer) {
        scripts.add(scriptContainer);
        scriptContainer.setCallback(message -> scriptManager.runScriptContainer(scriptContainer, message));
    }

    @Override
    public List<ScriptContainer> getAll() {
        return scripts;
    }

    @Override
    public void remove(ScriptContainer scriptContainer) {
        scripts.remove(scriptContainer);
    }

    @Override
    public void tick() {
        for (ScriptContainer script : scripts) {
            script.getTrigger().check();
        }
    }
}
