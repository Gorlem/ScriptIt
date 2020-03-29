package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.api.scripts.ScriptManager;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class ScriptManagerImpl implements Tickable, ScriptManager {
    private List<ScriptContainer> scripts = new ArrayList<>();

    @Override
    public void add(ScriptContainer scriptContainer) {
        scripts.add(scriptContainer);
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
            script.checkTrigger();
        }
    }
}
