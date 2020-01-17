package com.ddoerr.scriptit;

import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.scripts.ScriptThread;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.scripts.ThreadLifetimeManager;
import com.ddoerr.scriptit.triggers.Trigger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import org.apache.commons.lang3.StringUtils;

public class ScriptContainer implements Trigger {
    private LifeCycle lifeCycle = LifeCycle.Instant;
    private Trigger trigger;
    private String content = StringUtils.EMPTY;
    private Object lastResult;

    public ScriptContainer(Trigger trigger) {
        this.trigger = trigger;
    }

    public ScriptContainer(Trigger trigger, LifeCycle lifeCycle) {
        this.trigger = trigger;
        this.lifeCycle = lifeCycle;
    }

    public ScriptContainer(Trigger trigger, LifeCycle lifeCycle, String content) {
        this.trigger = trigger;
        this.lifeCycle = lifeCycle;
        this.content = content;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getLastResult() {
        return lastResult;
    }

    public Object run() {
        try {
            Script script = new ScriptBuilder()
                    .fromString(content)
                    .build();

            switch (lifeCycle) {
                case Instant:
                    lastResult = script.runInstantly();
                    break;
                case Threaded:
                    ScriptThread thread = script.runThreaded();
                    Resolver.getInstance().resolve(ThreadLifetimeManager.class).watch(thread);
                    lastResult = null;
                    break;
            }
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(e.getMessage()));
            e.printStackTrace();
        }

        return lastResult;
    }

    @Override
    public boolean canRun() {
        if (trigger == null)
            return false;

        return trigger.canRun();
    }

    @Override
    public void reset() {
        if (trigger != null)
            trigger.reset();
    }
}
