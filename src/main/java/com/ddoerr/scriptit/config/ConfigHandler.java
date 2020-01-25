package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.util.Debouncer;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.dependencies.Loadable;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.elements.HudElement;
import com.ddoerr.scriptit.elements.HudElementManager;
import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.scripts.Scripts;

import java.time.Duration;

public class ConfigHandler implements ConfigCallback, Loadable {
    private Config config = new Config();
    private Debouncer debouncer = new Debouncer(Duration.ofMillis(500), this::save);

    private Scripts scripts;
    private HudElementManager hudElementManager;

    public ConfigHandler() {
        scripts = Resolver.getInstance().resolve(Scripts.class);
        hudElementManager = Resolver.getInstance().resolve(HudElementManager.class);
    }

    public void save() {
        ConfigContainer configContainer = new ConfigContainer();

        configContainer.bindings = scripts.getAll();
        configContainer.elements = hudElementManager.getAll();

        config.write(configContainer);
    }

    public void load() {
        config.ensureConfigExists();
        ConfigContainer configContainer = config.read();

        for (ScriptContainer binding : configContainer.bindings) {
            scripts.add(binding);
        }

        for (HudElement element : configContainer.elements) {
            hudElementManager.add(element);
        }

        ConfigCallback.EVENT.register(this);
    }

    @Override
    public void saveConfig(Class<?> source) {
        debouncer.call();
    }
}
