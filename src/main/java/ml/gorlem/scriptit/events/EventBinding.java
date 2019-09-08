package ml.gorlem.scriptit.events;

import ml.gorlem.scriptit.api.events.Event;
import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import ml.gorlem.scriptit.api.scripts.Script;
import ml.gorlem.scriptit.api.scripts.ScriptBuilder;
import ml.gorlem.scriptit.api.scripts.ScriptThread;
import ml.gorlem.scriptit.callbacks.ConfigCallback;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.loader.EventContainer;
import ml.gorlem.scriptit.scripts.ScriptBinding;
import ml.gorlem.scriptit.scripts.ThreadLifetimeManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;

public class EventBinding {
    Event event;
    String scriptContent;

    public EventBinding(Event event, String scriptContent) {
        this.event = event;
        this.scriptContent = scriptContent;

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
