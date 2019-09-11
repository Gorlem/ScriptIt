package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.events.EventBinding;
import com.ddoerr.scriptit.scripts.ScriptBinding;

import java.util.ArrayList;
import java.util.Collection;

public class ConfigContainer {
    public Collection<HudElement> elements = new ArrayList<>();
    public Collection<ScriptBinding> bindings = new ArrayList<>();
    public Collection<EventBinding> events = new ArrayList<>();
}
