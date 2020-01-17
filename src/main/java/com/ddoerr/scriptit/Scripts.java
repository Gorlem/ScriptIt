package com.ddoerr.scriptit;

import net.minecraft.util.Tickable;

import java.util.*;

public class Scripts implements Tickable {
    public static final String KEYBIND_CATEGORY = "keybind";
    public static final String EVENT_CATEGORY = "event";
    public static final String ELEMENT_CATEGORY = "element";


    private Map<String, List<ScriptContainer>> scripts = new HashMap<>();

    public void add(String category, ScriptContainer scriptContainer) {
        if (scripts.containsKey(category)) {
            scripts.get(category).add(scriptContainer);
        } else {
            scripts.put(category, new ArrayList<>(Collections.singletonList(scriptContainer)));
        }
    }

    public List<ScriptContainer> getAll(String category) {
        if (!scripts.containsKey(category)) {
            return Collections.emptyList();
        }

        return scripts.get(category);
    }

    public void remove(String category, ScriptContainer scriptContainer) {
        scripts.get(category).remove(scriptContainer);
    }

    @Override
    public void tick() {
        for (List<ScriptContainer> category : scripts.values()) {
            for (ScriptContainer script : category) {
                if (script.canRun()) {
                    script.run();
                }
            }
        }
    }
}
