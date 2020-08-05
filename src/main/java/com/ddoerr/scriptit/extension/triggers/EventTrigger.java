package com.ddoerr.scriptit.extension.triggers;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.fields.Field;
import com.ddoerr.scriptit.fields.SelectionField;
import com.ddoerr.scriptit.fields.StringField;
import com.ddoerr.scriptit.triggers.AbstractTrigger;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EventTrigger extends AbstractTrigger {
    public static final Identifier IDENTIFIER = new Identifier(ScriptItMod.MOD_NAME, "event");
    public static final String EVENT_FIELD = "event";

    private ScriptItRegistry registry;

    private SelectionField eventField;

    public EventTrigger(ScriptItRegistry registry) {
        this.registry = registry;

        eventField = new SelectionField();
        eventField.setTitle(new LiteralText("Event ID"));
        eventField.setDescription(new LiteralText("Event which should trigger this script"));
        eventField.setTranslationPrefix("event");

        List<String> eventIds = registry.events.getIds().stream()
                .map(Identifier::toString)
                .collect(Collectors.toList());

        eventField.setValues(eventIds);
        fields.put(EVENT_FIELD, eventField);
    }

    @Override
    public void start() {
        Event event = registry.events.get(new Identifier(eventField.getValue()));
        if (event != null) {
            event.registerListener(callback);
        }
    }

    @Override
    public void stop() {
        Event event = registry.events.get(new Identifier(eventField.getValue()));
        if (event != null) {
            event.removeListener(callback);
        }
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public String toString() {
        return "on event " + eventField.getValue();
    }
}
