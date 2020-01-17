package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.ScriptContainer;
import com.ddoerr.scriptit.Scripts;
import com.ddoerr.scriptit.dependencies.Loadable;
import com.ddoerr.scriptit.api.util.Debouncer;
import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.dependencies.Resolver;
import com.ddoerr.scriptit.elements.HudElementManager;
import com.ddoerr.scriptit.callbacks.ConfigCallback;
import com.ddoerr.scriptit.events.EventBinding;
import com.ddoerr.scriptit.events.EventManager;

import java.time.Duration;

public class ConfigHandler implements ConfigCallback, Loadable {
    private Config config = new Config();
    private Debouncer debouncer = new Debouncer(Duration.ofMillis(500), this::save);

    private Scripts scripts;
    private HudElementManager hudElementManager;
    private EventManager eventManager;

    public ConfigHandler() {
        scripts = Resolver.getInstance().resolve(Scripts.class);
        hudElementManager = Resolver.getInstance().resolve(HudElementManager.class);
        eventManager = Resolver.getInstance().resolve(EventManager.class);
    }

    public void save() {
        ConfigContainer configContainer = new ConfigContainer();

        configContainer.bindings = scripts.getAll(Scripts.KEYBIND_CATEGORY);
        configContainer.elements = hudElementManager.getAll();
        configContainer.events = eventManager.getAll();

        config.write(configContainer);
    }

    public void load() {
        config.ensureConfigExists();
        ConfigContainer configContainer = config.read();

        for (ScriptContainer binding : configContainer.bindings) {
            scripts.add(Scripts.KEYBIND_CATEGORY, binding);
        }

        for (HudElement element : configContainer.elements) {
            hudElementManager.add(element);
        }

        for (EventBinding eventBinding : configContainer.events) {
            eventManager.setContent(eventBinding.getEvent(), eventBinding.getScriptContent());
        }

        ConfigCallback.EVENT.register(this);
    }

    @Override
    public void saveConfig(Class<?> source) {
        debouncer.call();
    }
}
