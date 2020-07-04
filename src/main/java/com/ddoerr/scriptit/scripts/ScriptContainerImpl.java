package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.api.libraries.Model;
import com.ddoerr.scriptit.api.scripts.LifeCycle;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.triggers.Trigger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import org.apache.commons.lang3.StringUtils;

public class ScriptContainerImpl implements ScriptContainer {
    private LifeCycle lifeCycle = LifeCycle.Instant;
    private Trigger trigger;
    private String content = StringUtils.EMPTY;
    private Object lastResult;
    private Pair<String, Model> library;

    private boolean isDisabled = false;

    public ScriptContainerImpl() {
    }

    public ScriptContainerImpl(Trigger trigger) {
        setTrigger(trigger);
    }

    public ScriptContainerImpl(Trigger trigger, LifeCycle lifeCycle) {
        setTrigger(trigger);
        setLifeCycle(lifeCycle);
    }

    public ScriptContainerImpl(Trigger trigger, LifeCycle lifeCycle, String content) {
        setTrigger(trigger);
        setLifeCycle(lifeCycle);
        setContent(content);
    }

    @Override
    public Trigger getTrigger() {
        return trigger;
    }

    @Override
    public void setTrigger(Trigger trigger) {
        if (this.trigger != null) {
            this.trigger.close();
        }

        this.trigger = trigger;
        trigger.setCallback(this::triggerCallback);
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Object getLastResult() {
        return lastResult;
    }

    @Override
    public void setLibrary(String name, Model library) {
        this.library = new Pair<>(name, library);
    }

    @Override
    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }

    @Override
    public void setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    @Override
    public void run() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (isDisabled) {
            return;
        }

        try {
            lastResult = new ScriptBuilder()
                    .fromString(content)
                    .withLibrary(library.getLeft(), library.getRight())
                    .lifeCycle(lifeCycle)
                    .run();
        } catch (Exception e) {
            disable();
            e.printStackTrace();
            minecraft.inGameHud.getChatHud().addMessage(new LiteralText(e.getMessage()));
            minecraft.inGameHud.getChatHud().addMessage(
                    new LiteralText("An error occurred while running this script. Script will be disabled until the script is saved again."));
        }
    }

    private void triggerCallback(Model library) {
        setLibrary("event", library);
        run();
    }

    @Override
    public void checkTrigger() {
        trigger.check();
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
                .append("; is ")
                .append(Formatting.YELLOW)
                .append(lifeCycle.toString())
                .append(Formatting.RESET)
                .append("; ")
                .append(StringUtils.abbreviate(content, 50));

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
}
