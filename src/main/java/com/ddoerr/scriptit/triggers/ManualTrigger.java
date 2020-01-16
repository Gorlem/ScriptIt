package com.ddoerr.scriptit.triggers;

public class ManualTrigger implements Trigger {
    boolean shouldActivate = false;

    public void activate() {
        shouldActivate = true;
    }

    @Override
    public boolean canRun() {
        return shouldActivate;
    }

    @Override
    public void reset() {
        shouldActivate = false;
    }
}
