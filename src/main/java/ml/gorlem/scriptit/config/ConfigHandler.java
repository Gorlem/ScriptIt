package ml.gorlem.scriptit.config;

import ml.gorlem.scriptit.dependencies.Loadable;
import ml.gorlem.scriptit.api.util.Debouncer;
import ml.gorlem.scriptit.api.hud.HudElement;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.elements.HudElementManager;
import ml.gorlem.scriptit.callbacks.ConfigCallback;
import ml.gorlem.scriptit.events.EventBinding;
import ml.gorlem.scriptit.events.EventManager;
import ml.gorlem.scriptit.scripts.ScriptBinding;
import ml.gorlem.scriptit.scripts.ScriptBindings;

import java.time.Duration;

public class ConfigHandler implements ConfigCallback, Loadable {
    private Config config = new Config();
    private Debouncer debouncer = new Debouncer(Duration.ofMillis(500), this::save);

    private ScriptBindings scriptBindings;
    private HudElementManager hudElementManager;
    private EventManager eventManager;

    public ConfigHandler() {
        scriptBindings = Resolver.getInstance().resolve(ScriptBindings.class);
        hudElementManager = Resolver.getInstance().resolve(HudElementManager.class);
        eventManager = Resolver.getInstance().resolve(EventManager.class);
    }

    public void save() {
        ConfigContainer configContainer = new ConfigContainer();

        configContainer.bindings = scriptBindings.getAll();
        configContainer.elements = hudElementManager.getAll();
        configContainer.events = eventManager.getAll();

        config.write(configContainer);
    }

    public void load() {
        config.ensureConfigExists();
        ConfigContainer configContainer = config.read();

        for (ScriptBinding binding : configContainer.bindings) {
            scriptBindings.add(binding);
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
