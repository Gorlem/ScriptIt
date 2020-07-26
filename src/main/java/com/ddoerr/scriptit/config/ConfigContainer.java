package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.api.hud.HudElementContainer;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;

import java.util.ArrayList;
import java.util.Collection;

public class ConfigContainer {
    public Collection<HudElementContainer> elements = new ArrayList<>();
    public Collection<ScriptContainer> bindings = new ArrayList<>();
}
