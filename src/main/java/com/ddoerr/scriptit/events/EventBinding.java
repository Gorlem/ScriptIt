package com.ddoerr.scriptit.events;

import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import org.apache.commons.lang3.StringUtils;

public class EventBinding {
    Event event;
    String scriptContent;

    public EventBinding(Event event) {
        this.event = event;
        this.scriptContent = StringUtils.EMPTY;

        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
    }

    public void run(NamespaceRegistry registry) {
        try {
            Script script = new ScriptBuilder()
                    .fromString(scriptContent)
                    .withRegistry(registry)
                    .setName(event.getName())
                    .build();

            script.runInstantly();
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(e.getMessage()));
            e.printStackTrace();
        }
    }

    public String getId() {
        return event.getName();
    }

    public String getScriptContent() {
        return scriptContent;
    }

    public void setScriptContent(String scriptContent) {
        this.scriptContent = scriptContent;
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
