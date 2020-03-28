package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.elements.HudElement;
import com.ddoerr.scriptit.scripts.ScriptContainer;

import java.util.ArrayList;
import java.util.Collection;

public class ConfigContainer {
    public Collection<HudElement> elements = new ArrayList<>();
    public Collection<ScriptContainer> bindings = new ArrayList<>();
}
