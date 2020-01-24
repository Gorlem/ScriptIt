package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.triggers.Trigger;
import net.minecraft.util.Tickable;

import java.util.*;

public class Scripts implements Tickable {
    private List<ScriptContainer> scripts = new ArrayList<>();

    public void add(ScriptContainer scriptContainer) {
        scripts.add(scriptContainer);
    }

    public List<ScriptContainer> getAll() {
        return scripts;
    }

    public void remove(ScriptContainer scriptContainer) {
        scripts.remove(scriptContainer);
    }

    @Override
    public void tick() {
        for (ScriptContainer script : scripts) {
            script.runIfPossible();
        }
    }
}
