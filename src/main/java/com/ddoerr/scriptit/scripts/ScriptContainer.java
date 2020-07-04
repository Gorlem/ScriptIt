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

public interface ScriptContainer {
    Trigger getTrigger();
    void setTrigger(Trigger trigger);

    String getContent();
    void setContent(String content);

    Object getLastResult();

    void setLibrary(String name, Model library);

    LifeCycle getLifeCycle();
    void setLifeCycle(LifeCycle lifeCycle);

    void run();
    void checkTrigger();

    boolean isDisabled();
    void disable();
    void enable();
}
