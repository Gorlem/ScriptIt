package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.hud.HudElementManager;
import com.ddoerr.scriptit.api.scripts.ScriptManager;
import com.ddoerr.scriptit.api.util.Debouncer;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.elements.HudElementContainer;
import com.ddoerr.scriptit.scripts.ScriptContainer;

import java.time.Duration;

public class ConfigHandler implements ConfigCallback, Loadable {
    private Config config = new Config();
    private Debouncer debouncer = new Debouncer(Duration.ofMillis(500), this::save);

    private ScriptManager scriptManager;
    private HudElementManager hudElementManager;

    public ConfigHandler(ScriptManager scriptManager, HudElementManager hudElementManager) {
        this.scriptManager = scriptManager;
        this.hudElementManager = hudElementManager;
    }

    public void save() {
        ConfigContainer configContainer = new ConfigContainer();

        configContainer.bindings = scriptManager.getAll();
        configContainer.elements = hudElementManager.getAll();

        config.write(configContainer);
    }

    public void load() {
        config.ensureConfigExists();
        ConfigContainer configContainer = config.read();

        for (ScriptContainer binding : configContainer.bindings) {
            scriptManager.add(binding);
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
