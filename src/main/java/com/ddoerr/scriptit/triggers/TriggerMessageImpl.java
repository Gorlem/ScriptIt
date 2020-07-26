package com.ddoerr.scriptit.triggers;

import com.ddoerr.scriptit.api.triggers.TriggerMessage;
import com.ddoerr.scriptit.api.libraries.Model;

import java.time.Duration;

public class TriggerMessageImpl implements TriggerMessage {
    private Model model;
    private Duration duration;

    public TriggerMessageImpl() {
    }

    public TriggerMessageImpl(Model model) {
        this.model = model;
    }

    public TriggerMessageImpl(Model model, Duration duration) {
        this.model = model;
        this.duration = duration;
    }

    @Override
    public Model getTriggerModel() {
        return model;
    }

    @Override
    public Duration getTimeout() {
        return duration;
    }
}
