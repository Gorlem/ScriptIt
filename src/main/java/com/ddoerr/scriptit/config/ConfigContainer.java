package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.elements.HudElementContainer;
import com.ddoerr.scriptit.scripts.ScriptContainer;

import java.util.ArrayList;
import java.util.Collection;

public class ConfigContainer {
    public Collection<HudElementContainer> elements = new ArrayList<>();
    public Collection<ScriptContainer> bindings = new ArrayList<>();
}
