package com.ddoerr.scriptit.api.scripts;

import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;

import java.util.function.Consumer;

public interface ScriptContainer {
    Trigger getTrigger();
    void setTrigger(Trigger trigger);

    Script getScript();
    void setScript(Script script);

    void setCallback(Consumer<TriggerMessage> callback);

    ContainedValue getLastResult();
    void setLastResult(ContainedValue value);

    boolean isDisabled();
    void disable();
    void enable();
}
