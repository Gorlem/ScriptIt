package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.libraries.Library;

public interface Trigger {
    boolean canRun();
    void reset();
    Library getAdditionalLibrary();
    void close();
}
