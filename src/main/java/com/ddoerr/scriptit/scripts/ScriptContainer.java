package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.api.libraries.Library;
import com.ddoerr.scriptit.api.scripts.LifeCycle;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.triggers.Trigger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

public class ScriptContainer {
    private LifeCycle lifeCycle = LifeCycle.Instant;
    private Trigger trigger;
    private String content = StringUtils.EMPTY;
    private Object lastResult;
    private Library library;

    private boolean isDisabled = false;

    public ScriptContainer() {
    }

    public ScriptContainer(Trigger trigger) {
        setTrigger(trigger);
    }

    public ScriptContainer(Trigger trigger, LifeCycle lifeCycle) {
        setTrigger(trigger);
        setLifeCycle(lifeCycle);
    }

    public ScriptContainer(Trigger trigger, LifeCycle lifeCycle, String content) {
        setTrigger(trigger);
        setLifeCycle(lifeCycle);
        setContent(content);
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        if (this.trigger != null) {
            this.trigger.close();
        }

        this.trigger = trigger;
        trigger.setCallback(this::triggerCallback);
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

    public void setLibrary(Library library) {
        this.library = library;
    }

    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public void run() {
        if (isDisabled) {
            return;
        }

        try {
            lastResult = new ScriptBuilder()
                    .fromString(content)
                    .withLibrary(library)
                    .lifeCycle(lifeCycle)
                    .run();
        } catch (Exception e) {
            disable();
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(e.getMessage()));
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
                    new LiteralText("An error occurred while running this script. Script will be disabled until the script is saved again."));
            e.printStackTrace();
        }
    }

    private void triggerCallback(Library library) {
        setLibrary(library);
        run();
    }

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

    public boolean isDisabled() {
        return isDisabled;
    }

    public void disable() {
        isDisabled = true;
    }

    public void enable() {
        isDisabled = false;
    }
}
