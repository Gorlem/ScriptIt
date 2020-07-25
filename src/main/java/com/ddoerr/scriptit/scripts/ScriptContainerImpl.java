package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

public class ScriptContainerImpl implements ScriptContainer {
    private Trigger trigger;
    private Script script;

    private Consumer<TriggerMessage> callback = triggerMessage -> {};

    private ContainedValue lastResult;
    private boolean isDisabled = false;
    private boolean isHidden = false;

    public ScriptContainerImpl() {
    }

    public ScriptContainerImpl(Trigger trigger, Script script) {
        setTrigger(trigger);
        setScript(script);
    }

    @Override
    public Trigger getTrigger() {
        return trigger;
    }

    @Override
    public void setTrigger(Trigger trigger) {
        if (this.trigger != null) {
            this.trigger.stop();
        }

        this.trigger = trigger;
        trigger.setCallback(callback);
    }

    @Override
    public ContainedValue getLastResult() {
        return lastResult;
    }

    @Override
    public void setLastResult(ContainedValue lastResult) {
        this.lastResult = lastResult;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        if (trigger != null) {
            stringBuilder
                    .append("triggers ")
                    .append(Formatting.YELLOW)
                    .append(trigger.toString());
        } else {
            stringBuilder.append("no activation trigger");
        }

        stringBuilder
                .append(Formatting.RESET)
                .append("; ")
                .append(StringUtils.abbreviate(script.getScriptSource().getContent(), 50));

        return stringBuilder.toString();
    }

    @Override
    public boolean isDisabled() {
        return isDisabled;
    }

    @Override
    public void disable() {
        isDisabled = true;
    }

    @Override
    public void enable() {
        isDisabled = false;
    }

    @Override
    public Script getScript() {
        return script;
    }

    @Override
    public void setScript(Script script) {
        this.script = script;
    }

    @Override
    public void setCallback(Consumer<TriggerMessage> callback) {
        this.callback = callback;
        if (trigger != null) {
            trigger.setCallback(callback);
        }
    }
}
