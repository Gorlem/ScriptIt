package com.ddoerr.scriptit.api.triggers;

import com.ddoerr.scriptit.api.triggers.Trigger;
import net.minecraft.util.Identifier;

import java.util.Map;

public interface TriggerFactory {
    Trigger createTrigger(Identifier identifier, Map<String, String> data);
}
