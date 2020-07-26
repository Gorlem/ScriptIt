package com.ddoerr.scriptit.api.triggers;

import com.ddoerr.scriptit.api.libraries.Model;

import java.time.Duration;

public interface TriggerMessage {
    Model getTriggerModel();
    Duration getTimeout();
}
