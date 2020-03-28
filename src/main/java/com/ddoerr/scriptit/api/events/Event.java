package com.ddoerr.scriptit.api.events;

import com.ddoerr.scriptit.api.libraries.Model;

public interface Event {
    void dispatch();
    void dispatch(Model model);
    String getName();
}
