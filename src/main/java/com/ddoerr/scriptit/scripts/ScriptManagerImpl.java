package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.api.scripts.ScriptManager;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScriptManagerImpl implements ScriptManager {
    private ScriptItRegistry registry;

    public ScriptManagerImpl(ScriptItRegistry registry) {
        this.registry = registry;
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

        if (triggerMessage.getTimeout() == null) {
            future.thenAccept(scriptContainer::setLastResult);
        } else {
            Duration timeout = triggerMessage.getTimeout();
            try {
                ContainedValue containedValue = future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
                scriptContainer.setLastResult(containedValue);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
