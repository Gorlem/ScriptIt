package com.ddoerr.scriptit.scripts;

import com.ddoerr.scriptit.LifeCycle;
import com.ddoerr.scriptit.ScriptContainer;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptThread;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.triggers.KeybindingTrigger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.text.LiteralText;

public class ScriptBinding {
    ScriptContainer scriptContainer;

    public ScriptBinding(KeyBinding keyBinding, String scriptContent) {
        scriptContainer = new ScriptContainer(new KeybindingTrigger(keyBinding), LifeCycle.Threaded, scriptContent);

        ConfigCallback.EVENT.invoker().saveConfig(this.getClass());
    }

    public boolean wasPressed() {
        return scriptContainer.canRun();
    }

    public void run() {
        scriptContainer.run();
        scriptContainer.reset();
    }

    public String getId() {
        return ((KeybindingTrigger)scriptContainer.getTrigger()).getKeyBinding().getId();
    }

    public KeyBinding getKeyBinding() {
        return ((KeybindingTrigger)scriptContainer.getTrigger()).getKeyBinding();
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
        if (!(obj instanceof  ScriptBinding)) {
            return false;
        }

        return getId().equals(((ScriptBinding) obj).getId());
    }
}
