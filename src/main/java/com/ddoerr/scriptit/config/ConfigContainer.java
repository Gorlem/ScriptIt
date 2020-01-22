package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.scripts.ScriptContainer;
import com.ddoerr.scriptit.api.hud.HudElement;

import java.util.ArrayList;
import java.util.Collection;

public class ConfigContainer {
    public Collection<HudElement> elements = new ArrayList<>();
    public Collection<ScriptContainer> bindings = new ArrayList<>();
    public Collection<ScriptContainer> events = new ArrayList<>();
}
