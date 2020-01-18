package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.LifeCycle;
import com.ddoerr.scriptit.ScriptContainer;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.triggers.ManualTrigger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import org.apache.commons.lang3.StringUtils;

public class EventBinding {
    Event event;
    ScriptContainer scriptContainer;

    public EventBinding(Event event) {
        this.event = event;

        scriptContainer = new ScriptContainer(new ManualTrigger(), LifeCycle.Instant);
        scriptContainer.setName(event.getName());

        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
    }

    public void run(NamespaceRegistry registry) {
        scriptContainer.setNamespaceRegistry(registry);
        scriptContainer.run();
        scriptContainer.reset();
    }

    public String getId() {
        return scriptContainer.getName();
    }

    public String getScriptContent() {
        return scriptContainer.getContent();
    }

    public void setScriptContent(String scriptContent) {
        scriptContainer.setContent(scriptContent);
        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EventBinding)) {
            return false;
        }

        return getId().equals(((EventBinding) obj).getId());
    }

    public Event getEvent() {
        return event;
    }
}
