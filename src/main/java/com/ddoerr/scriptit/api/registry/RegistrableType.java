package com.ddoerr.scriptit.api.registry;

import com.ddoerr.scriptit.api.hud.HudElementProvider;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.libraries.Model;

public interface RegistrableType<T> {
    RegistrableType<Model> Library = new RegistrableType<Model>() {};
    RegistrableType<Object> Event = new RegistrableType<Object>() {};
    RegistrableType<Language> Language = new RegistrableType<Language>() {};
    RegistrableType<HudElementProvider> HudElement = new RegistrableType<HudElementProvider>() {};
}
