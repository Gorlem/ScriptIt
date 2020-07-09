package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.hud.HudElementManager;
import com.ddoerr.scriptit.api.scripts.ScriptContainerManager;
import com.ddoerr.scriptit.api.util.Debouncer;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.api.hud.HudElementContainer;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;

import java.time.Duration;

public class ConfigHandler implements ConfigCallback, Loadable {
    private Config config;
    private Debouncer debouncer = new Debouncer(Duration.ofMillis(500), this::save);

    private ScriptContainerManager scriptContainerManager;
    private HudElementManager hudElementManager;

    public ConfigHandler(Config config, ScriptContainerManager scriptContainerManager, HudElementManager hudElementManager) {
        this.config = config;
        this.scriptContainerManager = scriptContainerManager;
        this.hudElementManager = hudElementManager;
    }

    public void save() {
        ConfigContainer configContainer = new ConfigContainer();

        configContainer.bindings = scriptContainerManager.getAll();
        configContainer.elements = hudElementManager.getAll();

        config.write(configContainer);
    }

    public void load() {
        config.ensureConfigExists();
        ConfigContainer configContainer = config.read();

        for (ScriptContainer binding : configContainer.bindings) {
            scriptContainerManager.add(binding);
        }

        for (HudElementContainer element : configContainer.elements) {
            hudElementManager.add(element);
        }

        ConfigCallback.EVENT.register(this);
    }

    @Override
    public void saveConfig(Class<?> source) {
        debouncer.call();
    }
}
