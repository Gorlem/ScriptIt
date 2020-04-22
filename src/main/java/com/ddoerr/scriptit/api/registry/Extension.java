package com.ddoerr.scriptit.api.registry;

import com.ddoerr.scriptit.api.events.Event;
import com.ddoerr.scriptit.api.hud.HudElementProvider;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.libraries.Model;

public interface Extension {
    <T> Extension register(Class<T> registryType, String name, Class<? extends T> model);

    default Extension library(String name, Class<? extends Model> model) {
        return register(Model.class, name, model);
    }

    default Extension event(String name, Class<? extends Event> event) {
        return register(Event.class, name, event);
    }

    default Extension language(String name, Class<? extends Language> language) {
        return register(Language.class, name, language);
    }

    default Extension hudElement(String name, Class<? extends HudElementProvider> hudElement) {
        return register(HudElementProvider.class, name, hudElement);
    }
}
