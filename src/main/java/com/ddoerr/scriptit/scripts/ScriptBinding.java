package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptThread;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.text.LiteralText;

public class ScriptBinding {
    KeyBinding keyBinding;
    String scriptContent;

    public ScriptBinding(KeyBinding keyBinding, String scriptContent) {
        this.keyBinding = keyBinding;
        this.scriptContent = scriptContent;

        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
    }

    public boolean wasPressed() {
        return keyBinding.wasPressed();
    }

    public void run() {
        try {
            Script script = new ScriptBuilder()
                    .fromString(scriptContent)
                    .build();

            ScriptThread thread = script.runThreaded();
            Resolver.getInstance().resolve(ThreadLifetimeManager.class).watch(thread);
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(e.getMessage()));
            e.printStackTrace();
        }
    }

    public String getId() {
        return keyBinding.getId();
    }

    public KeyBinding getKeyBinding() {
        return keyBinding;
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
        if (!(obj instanceof  ScriptBinding)) {
            return false;
        }

        return getId().equals(((ScriptBinding) obj).getId());
    }
}
