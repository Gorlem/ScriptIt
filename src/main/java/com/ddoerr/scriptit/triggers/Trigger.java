package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.libraries.Library;

import java.util.function.Consumer;

public interface Trigger {
    void setCallback(Consumer<Library> callback);
    void check();
    void close();
}
