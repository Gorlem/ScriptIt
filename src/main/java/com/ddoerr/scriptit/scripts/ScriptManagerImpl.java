package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.scripts.*;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScriptManagerImpl implements ScriptManager {
    private ScriptItRegistry registry;
    private MinecraftClient minecraft;

    private List<RunningScript> runningScripts = new ArrayList<>();

    public ScriptManagerImpl(ScriptItRegistry registry, MinecraftClient minecraft) {
        this.registry = registry;
        this.minecraft = minecraft;
    }

    @Override
    public CompletableFuture<ContainedValue> runScript(Script script) {
        Language language = registry.languages.get(script.getLanguage());
        return language.runScript(script);
    }

    @Override
    public void runScriptContainer(ScriptContainer scriptContainer, TriggerMessage triggerMessage) {
        if (scriptContainer.isDisabled() || triggerMessage == null) {
            return;
        }

        ScriptBuilder scriptBuilder = new ScriptBuilder(scriptContainer.getScript())
                .withLibrary("event", triggerMessage.getTriggerModel());

        CompletableFuture<ContainedValue> future = runScript(scriptBuilder);

        RunningScriptImpl runningScript = new RunningScriptImpl(scriptContainer, future);
        runningScripts.add(runningScript);

        CompletableFuture<Void> handledFuture = future.exceptionally((throwable) -> {
            scriptContainer.disable();
            throwable.printStackTrace();

            minecraft.inGameHud.getChatHud().addMessage(new LiteralText(throwable.getMessage()));
            minecraft.inGameHud.getChatHud().addMessage(new TranslatableText("scriptit:scripts.exception", scriptContainer.getScript().getName()));

            return null;
        }).thenAccept(scriptContainer::setLastResult)
            .thenRun(() -> runningScripts.remove(runningScript));

        if (triggerMessage.getTimeout() != null) {
            Duration timeout = triggerMessage.getTimeout();
            try {
                handledFuture.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<RunningScript> getRunningScripts() {
        return runningScripts;
    }

    @Override
    public int stopScripts(String name) {
        int count = 0;
        for (RunningScript runningScript : runningScripts) {
            if (runningScript.getScriptContainer().getScript().getName().equals(name)) {
                count++;
                runningScript.getFuture().cancel(true);
            }
        }
        return count;
    }

    @Override
    public int stopAllScripts() {
        int count = 0;
        for (RunningScript runningScript : runningScripts) {
            count++;
            runningScript.getFuture().cancel(true);
        }
        return count;
    }
}
