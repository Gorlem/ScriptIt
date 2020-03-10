package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.api.scripts.ThreadLifetimeManager;
import com.google.common.collect.ImmutableList;
import com.ddoerr.scriptit.api.scripts.ScriptThread;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class ThreadLifetimeManagerImpl implements Tickable, ThreadLifetimeManager {
    private List<ScriptThread> threads = new ArrayList<>();

    public void tick() {
        for (ScriptThread thread : ImmutableList.copyOf(threads)) {
            boolean result = thread.resume();

            if (!result) {
                threads.remove(thread);
            }
        }
    }

    @Override
    public void watch(ScriptThread thread) {
        threads.add(thread);
    }

    @Override
    public int stopAll() {
        int amount = threads.size();
        threads.clear();
        return amount;
    }
}
