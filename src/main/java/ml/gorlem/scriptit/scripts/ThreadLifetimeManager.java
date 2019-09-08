package ml.gorlem.scriptit.scripts;

import com.google.common.collect.ImmutableList;
import ml.gorlem.scriptit.api.scripts.ScriptThread;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class ThreadLifetimeManager implements Tickable {
    private List<ScriptThread> threads = new ArrayList<>();

    public void tick() {
        for (ScriptThread thread : ImmutableList.copyOf(threads)) {
            boolean result = thread.resume();

            if (!result) {
                threads.remove(thread);
            }
        }
    }

    public void watch(ScriptThread thread) {
        threads.add(thread);
    }

    public int stopAll() {
        int amount = threads.size();
        threads.clear();
        return amount;
    }
}
