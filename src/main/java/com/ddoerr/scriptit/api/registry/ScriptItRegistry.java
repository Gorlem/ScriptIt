package com.ddoerr.scriptit.api.registry;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.hud.HudElement;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.libraries.Model;
import com.ddoerr.scriptit.api.triggers.Trigger;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.function.Supplier;

public class ScriptItRegistry extends SimpleRegistry<Registry<?>> {
    public final SimpleRegistry<Model> libraries = new SimpleRegistry<>();
    public final DefaultedRegistry<Language> languages = new DefaultedRegistry<>(ScriptItMod.MOD_NAME + ":lua");
    public final SimpleRegistry<Event> events = new SimpleRegistry<>();
    public final SimpleRegistry<HudElement> hudElements = new SimpleRegistry<>();
    public final SimpleRegistry<Supplier<Trigger>> triggers = new SimpleRegistry<>();

    public ScriptItRegistry() {
        add(new Identifier(ScriptItMod.MOD_NAME, "libraries"), libraries);
        add(new Identifier(ScriptItMod.MOD_NAME, "languages"), languages);
        add(new Identifier(ScriptItMod.MOD_NAME, "events"), events);
        add(new Identifier(ScriptItMod.MOD_NAME, "hud_elements"), hudElements);
        add(new Identifier(ScriptItMod.MOD_NAME, "triggers"), triggers);
    }
}
