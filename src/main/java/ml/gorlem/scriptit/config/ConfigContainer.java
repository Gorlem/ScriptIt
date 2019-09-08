package ml.gorlem.scriptit.config;

import ml.gorlem.scriptit.elements.IconHudElement;
import ml.gorlem.scriptit.elements.TextHudElement;
import ml.gorlem.scriptit.events.EventBinding;
import ml.gorlem.scriptit.scripts.ScriptBinding;
import ml.gorlem.scriptit.api.hud.HudElement;
import ml.gorlem.scriptit.api.util.KeyBindingHelper;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ConfigContainer {
    public Collection<HudElement> elements = new ArrayList<>();
    public Collection<ScriptBinding> bindings = new ArrayList<>();
    public Collection<EventBinding> events = new ArrayList<>();
}
